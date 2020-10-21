package com.example.htc_lab;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Bundle;
import android.os.StrictMode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String DownloadJSON (String http) {
        InputStream in;
        BufferedReader br;
        try {
            //Connect to http
            URL url = new URL(http);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setConnectTimeout(3000);
            httpConn.setReadTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.connect();

            //Open InputStream and read JSON from http
            //Build it to String
            int resCode = httpConn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                br = new BufferedReader(new InputStreamReader(in));

                StringBuilder sb= new StringBuilder();
                String s;
                while((s= br.readLine())!= null) {
                    sb.append(s);
                    sb.append("\n");
                }
                return sb.toString();
            } else {
                throw new IOException("No response received.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "notwork";
        }
    }
    public static class CompaniesAdapter extends
            RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

        private List<Companies> companiesList;

        public CompaniesAdapter(List<Companies> companies) {
            this.companiesList = companies;
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView name;
            public TextView age;
            public TextView competences;
            public TextView emp_name;
            public TextView emp_pn;
            public TextView emp_skills;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                name = itemView.findViewById(R.id.name);
                age = itemView.findViewById(R.id.age);
                competences = itemView.findViewById(R.id.competences);
                emp_name = itemView.findViewById(R.id.emp_name);
                emp_pn = itemView.findViewById(R.id.emp_pn);
                emp_skills = itemView.findViewById(R.id.emp_skills);
            }
        }

        // Store a member variable for the contacts
        @NonNull
        @Override
        public CompaniesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);


            // Inflate the custom layout
            View companyView = inflater.inflate(R.layout.company_item, parent, false);

            // Return a new holder instance
            return new ViewHolder(companyView);
        }

        // Involves populating data into the item through holder
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(CompaniesAdapter.ViewHolder holder, int position) {
            // Get the data model based on position
            Employee Comp = companiesList.get(0).getCompany().getEmployees().get(position);
            // Set item views based on your views and data model

            TextView textViewE_Name = holder.emp_name;
            TextView textViewE_pn = holder.emp_pn;
            TextView textViewE_Competences = holder.emp_skills;

            //Check fields and fill
            StringBuilder s = new StringBuilder();
                if (Comp.getName() != null)
                    textViewE_Name.setText(Comp.getName());
                else
                    textViewE_Name.setText("-");

                if (Comp.getPhoneNumber() != null)
                    textViewE_pn.setText(Comp.getPhoneNumber());
                else
                    textViewE_pn.setText("-");

                if (Comp.getSkills() != null) {
                    for (int i = 0; i < Comp.getSkills().size(); i++) {
                        s.append(Comp.getSkills().get(i));
                        s.append(" ");
                    }
                    textViewE_Competences.setText(s);
                }
                else
                    textViewE_Competences.setText("-");

        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {
            int a;
            if(companiesList.get(0).getCompany().getEmployees() != null &&
              !companiesList.get(0).getCompany().getEmployees().isEmpty())
            { a = companiesList.get(0).getCompany().getEmployees().size(); }
            else { a = 0; }
            return a;
        }
    }

    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //To work on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Download json
        String url = "http://www.mocky.io/v2/5ddcd3673400005800eae483";
        String JSON = DownloadJSON(url);

        //Create gson
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Companies JCompany = gson.fromJson(JSON, Companies.class);
        ArrayList<Companies> arrayOfCompanies = new ArrayList<>();
        arrayOfCompanies.add(JCompany);
        System.out.println(JCompany.getCompany().getEmployees().get(0).getName());

        CompaniesAdapter adapter = new CompaniesAdapter(arrayOfCompanies);
        RecyclerView recyclerView = findViewById(R.id.rvCompanies);

        //Fill company properties to layout
        TextView setName = findViewById(R.id.name);
        setName.setText(JCompany.getCompany().getName());
        TextView setAge = findViewById(R.id.age);
        setAge.setText(JCompany.getCompany().getAge());

        StringBuilder setCompArr = new StringBuilder();
        for (int i = 0; i < JCompany.getCompany().getCompetences().size(); i++) {
            setCompArr.append(JCompany.getCompany().getCompetences().get(i)).append(" ");
        }
        TextView setCompetences = findViewById(R.id.competences);
        setCompetences.setText(setCompArr.toString());

        //Helps to display items in the correct order
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}