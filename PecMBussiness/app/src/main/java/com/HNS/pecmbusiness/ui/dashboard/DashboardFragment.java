package com.HNS.pecmbusiness.ui.dashboard;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.HNS.pecmbusiness.MainActivity;
import com.HNS.pecmbusiness.R;
import com.HNS.pecmbusiness.object.customer;
import com.HNS.pecmbusiness.object.dishlist;
import com.HNS.pecmbusiness.object.menu;
import com.HNS.pecmbusiness.object.order;
import com.HNS.pecmbusiness.registration.StartActivity;
import com.HNS.pecmbusiness.sharedpreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DashboardFragment extends Fragment {

    public static ArrayList<order> activelist = new ArrayList<>();
    public static ArrayList<order> unpaidlist = new ArrayList<>();
    public static dashboardunpaidadapter unpaidadapter = new dashboardunpaidadapter();
    public static dashboardactiveadapter activeadapter = new dashboardactiveadapter();
    private static View root;
    private static ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
    private static ParseQuery parseQuery = new ParseQuery(MainActivity.ShopID + "orders");
    private static SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
    String TAG = "";
    private RecyclerView activerecycler, unpaidrecycler;

    public static void activelivequery(final Context context) {
        parseQuery.whereEqualTo("complete", "0");
        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
            @Override
            public void onEvents(final ParseQuery<ParseObject> query, SubscriptionHandling.Event event, final ParseObject object) {
                final Handler handler = new Handler(Looper.getMainLooper());
                if (event.name().equals("CREATE")) {
                    handler.post(new Runnable() {
                        public void run() {
                            order order = new order();
                            Gson gson = new Gson();
                            ArrayList<com.HNS.pecmbusiness.object.menu> menulist = gson.fromJson(object.getString("menulist"), new TypeToken<ArrayList<menu>>() {
                            }.getType());
                            order.setMenulist(menulist);
                            order.setXcoord(object.getInt("xcoord"));
                            order.setYcoord(object.getInt("ycoord"));
                            customer customer = gson.fromJson(object.getString("customer"), com.HNS.pecmbusiness.object.customer.class);
                            order.setCustomer(customer);
                            order.setAmount(object.getInt("amount"));
                            order.setMessage(object.getString("message"));
                            order.setOrderid(object.getObjectId());
                            order.setDate(object.getCreatedAt());
                            activelist.add(order);
                            activeadapter.notifyItemInserted(dishlist.dishlist.size() - 1);
                            activeadapter.notifyItemRangeChanged(0, dishlist.dishlist.size());
                            try {
                                NotificationManagerCompat.from(context).cancel(1);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
                                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                                        .setContentTitle("New Order")
                                        .setContentText("You have received a new order")
                                        .setPriority(NotificationCompat.PRIORITY_MAX);
                                NotificationManagerCompat.from(context).notify(1, builder.build());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                if (event.name().equals("DELETE")) {
                    handler.post(new Runnable() {
                        public void run() {
                            activeadapter.notifyItemRangeChanged(0, DashboardFragment.activelist.size());
                            activeadapter.notifyItemRemoved(getposition(object.getObjectId()));
                            try {
                                NotificationManagerCompat.from(context).cancel(2);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
                                builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                                        .setContentTitle("An order has been cancelled")
                                        .setContentText(notifmsg(object.getString("menulist")))
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notifmsg(object.getString("menulist"))))
                                        .setPriority(NotificationCompat.PRIORITY_MAX);
                                NotificationManagerCompat.from(context).notify(2, builder.build());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private static String notifmsg(String menu) {
        Gson gson = new Gson();
        ArrayList<menu> menulist = gson.fromJson(menu, new TypeToken<ArrayList<menu>>() {
        }.getType());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < menulist.size(); i++) {
            menu menu1 = menulist.get(i);
            sb.append(menu1.getDish().getDishname() + " x " + menu1.getQuatity());
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static int getposition(String orderid) {
        int i;
        order order;
        for (i = 0; i < activelist.size(); i++) {
            order = activelist.get(i);
            if (order.getOrderid().equals(orderid))
                break;
        }
        return i;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        activerecycler = root.findViewById(R.id.activerecycler);
        unpaidrecycler = root.findViewById(R.id.unpaidrecycler);
        root.findViewById(R.id.unpaidlayout).setVisibility(View.GONE);
        sharedpreferences.getunpaidlist(getContext());
        parseLiveQueryClient.connectIfNeeded();
        new loader().execute(getContext());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        root.findViewById(R.id.unpaidlayout).setVisibility(View.GONE);
        new loader().execute(getContext());
    }

    public boolean checknetworkaccess() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Callable<Boolean> task = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                Looper.prepare();
                InetAddress address;
                try {
                    address = InetAddress.getByName("www.google.com");
                } catch (IOException e) {
                    Log.e(TAG, "call: ioexception", e);
                    return false;
                }
                return !address.getHostAddress().equals("");
            }
        };
        Future<Boolean> future = executorService.submit(task);
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            Log.e(TAG, "checknetworkaccess: timeot", e);
            Toast.makeText(getContext(), "Please check you internet connection", Toast.LENGTH_LONG).show();
            return false;
        } catch (InterruptedException e) {
            Log.e(TAG, "checknetworkaccess: interrupt", e);
            Toast.makeText(getContext(), "Please check you internet connection", Toast.LENGTH_LONG).show();
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "checknetworkaccess: execution", e);
            Toast.makeText(getContext(), "Please check you internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private class loader extends AsyncTask<Context, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            root.findViewById(R.id.loading_panel).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Context... contexts) {
            if (StartActivity.getConnection(contexts[0]) && checknetworkaccess()) {
                activelist.clear();
                try {
                    parseQuery.whereEqualTo("complete", "0");
                    List<ParseObject> results = parseQuery.find();
                    for (ParseObject result : results) {
                        order order = new order();
                        Gson gson = new Gson();
                        ArrayList<com.HNS.pecmbusiness.object.menu> menulist = gson.fromJson(result.getString("menulist"), new TypeToken<ArrayList<menu>>() {
                        }.getType());
                        order.setMenulist(menulist);
                        Log.d("", "doInBackground: " + result.getString("customer"));
                        order.setXcoord(result.getInt("xcoord"));
                        order.setYcoord(result.getInt("ycoord"));
                        customer customer = gson.fromJson(result.getString("customer"), com.HNS.pecmbusiness.object.customer.class);
                        order.setCustomer(customer);
                        order.setAmount(result.getInt("amount"));
                        order.setMessage(result.getString("message"));
                        order.setOrderid(result.getObjectId());
                        order.setDate(result.getCreatedAt());
                        DashboardFragment.activelist.add(order);
                    }

                } catch (ParseException e) {
                    Log.e(TAG, "doInBackground: parse", e);
                }
            } else {
                cancel(true);
                root.findViewById(R.id.loading_panel).setVisibility(View.GONE);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sharedpreferences.getunpaidlist(getContext());
            if (!unpaidlist.isEmpty()) {
                root.findViewById(R.id.unpaidlayout).setVisibility(View.VISIBLE);
                unpaidrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                unpaidrecycler.addItemDecoration(new DividerItemDecoration(root.getContext(), LinearLayoutManager.VERTICAL));
                unpaidrecycler.setAdapter(unpaidadapter);
                unpaidadapter.notifyDataSetChanged();
            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            activerecycler.setLayoutManager(linearLayoutManager);
            activerecycler.addItemDecoration(new DividerItemDecoration(root.getContext(), LinearLayoutManager.VERTICAL));
            activerecycler.setAdapter(activeadapter);
            DashboardFragment.activeadapter.notifyItemRangeChanged(0, dishlist.dishlist.size());
            activeadapter.notifyDataSetChanged();
            root.findViewById(R.id.loading_panel).setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getContext(), "Please check you internet connection", Toast.LENGTH_LONG).show();
            sharedpreferences.getunpaidlist(getContext());
            if (!unpaidlist.isEmpty()) {
                root.findViewById(R.id.unpaidlayout).setVisibility(View.VISIBLE);
                unpaidrecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                unpaidrecycler.addItemDecoration(new DividerItemDecoration(root.getContext(), LinearLayoutManager.VERTICAL));
                unpaidrecycler.setAdapter(unpaidadapter);
                unpaidadapter.notifyDataSetChanged();
            }
        }
    }
}