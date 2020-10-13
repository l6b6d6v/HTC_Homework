package com.example.htc_lab;

import android.content.Context;

import android.os.Bundle;
import android.util.Log;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static android.util.Log.e;

public class MainActivity extends AppCompatActivity {


    public static class CompaniesAdapter extends
            RecyclerView.Adapter<CompaniesAdapter.ViewHolder> {

        private List<Company> mComs;

        public CompaniesAdapter(List<Company> mComs) {
            this.mComs = mComs;
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

                name = (TextView) itemView.findViewById(R.id.name);
                age = (TextView) itemView.findViewById(R.id.age);
                competences = (TextView) itemView.findViewById(R.id.competences);
                emp_name = (TextView) itemView.findViewById(R.id.emp_name);
                emp_pn = (TextView) itemView.findViewById(R.id.emp_pn);
                emp_skills = (TextView) itemView.findViewById(R.id.emp_skills);
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
        @Override
        public void onBindViewHolder(CompaniesAdapter.ViewHolder holder, int position) {
            // Get the data model based on position
            Company Comp = mComs.get(position);
            // Set item views based on your views and data model
            TextView textViewName = holder.name;
            textViewName.setText(Comp.getName());
            TextView textViewAge = holder.age;
            textViewAge.setText(Comp.getName());

            TextView textViewCompetences = holder.competences;
            textViewCompetences.setText((CharSequence) Comp.getCompetences());

            /*TextView textViewE_Name = holder.emp_name;
            textViewE_Name.setText(Emp.getName());
            TextView textViewE_pn = holder.emp_pn;
            textViewE_pn.setText(Emp.getPhoneNumber());

            TextView textViewE_Competences = holder.emp_skills;
            textViewE_Competences.setText((CharSequence) Emp.getSkills());*/
        }

        // Returns the total count of items in the list
        @Override
        public int getItemCount() {

            int a;

            if(mComs != null && !mComs.isEmpty()) {
                a = mComs.size();
            }
            else {

                a = 0;

            }
                return a;
        }
    }

    public static class IOUtils {

        public static void closeQuietly(InputStream in)  {
            try {
                in.close();
            }catch (Exception e) { e.printStackTrace(); }
        }

        public static void closeQuietly(Reader reader)  {
            try {
                reader.close();
            }catch (Exception e) { e.printStackTrace(); }
        }

    }

    /* Called when the activity is first created. */
    //@SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String JSON = "";
        InputStream in = null;
        BufferedReader br= null;
        try {
            URL url = new URL("https://www.mocky.io/v2/5ddcd3673400005800eae483");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
                br= new BufferedReader(new InputStreamReader(in));

                StringBuilder sb= new StringBuilder();
                String s;
                while((s= br.readLine())!= null) {
                    sb.append(s);
                    sb.append("\n");
                }
                JSON = sb.toString();
            } else {
                JSON = "notwork";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(br);
        }



            e("TAG", JSON);
            //Create gson
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Company JCompany = gson.fromJson(JSON, Company.class);

            ArrayList<Company> arrayOfCompanies = new ArrayList<>();
            arrayOfCompanies.add(JCompany);
            CompaniesAdapter adapter = new CompaniesAdapter(arrayOfCompanies);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvCompanies);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

    }
}