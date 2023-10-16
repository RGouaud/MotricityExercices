package com.projet15.lazer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.MyViewHolder>  {
    List <File> listeFichier;
    boolean [] isChecked;
    CheckBox _toutSelectionne;
    boolean _modifierEtatCheckBox = true;

    public MyFileAdapter(File[] tabFic, CheckBox c){
        _toutSelectionne = c;
        listeFichier = Arrays.asList(tabFic);
        isChecked = new boolean[getItemCount()];
        for (int i = 0; i< getItemCount();i++){
            isChecked[i] = false;
        }

    }
    public void selectAll (boolean boxChecked){
        for( int i = 0; i < isChecked.length ; i++){
            isChecked[i] = boxChecked;
        }
    }

    public List<File> getFileSelected(){
        List<File>  listeFichierSelectioner = new ArrayList<File>() ;
        for(int i = 0 ; i < this.isChecked.length; i++){
            if(isChecked[i]){
                listeFichierSelectioner.add(listeFichier.get(i));
                isChecked[i] = false;
            }

        }

        return listeFichierSelectioner;
    }

    public void selection(int position){
        isChecked[position] = !isChecked[position];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_file_list_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.display(listeFichier.get(position));
    }

    @Override
    public int getItemCount() {
        return listeFichier.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder   implements View.OnClickListener{
        private TextView _filenameView;
        private LinearLayout _layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            _filenameView = (TextView) itemView.findViewById(R.id.FILENAMEID);
            _layout = (LinearLayout) itemView.findViewById(R.id.LinearLayoutID);
        }
        void display(File fichierCourant){
            _filenameView.setText(fichierCourant.getName());
            _filenameView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int i = getAdapterPosition(); //getItemId renvoi la position de -1 à taille liste
            selection(i);

            if(isChecked[i]){
                _filenameView.setTypeface(null, Typeface.BOLD);
                _layout.setBackgroundColor(Color.parseColor("#DDDDDD"));
                Log.e("ISCHECKED?",listeFichier.get(i).getName()+ " is checked");
            }
            else{
                _filenameView.setTypeface(null, Typeface.NORMAL);
                _layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            boolean toutCoche = true;
            for (int a = 0; a < isChecked.length; a ++){
                if (isChecked[a] == false){
                    toutCoche = false;
                }
            }

            _modifierEtatCheckBox = false;


            if (toutCoche == true){
                _toutSelectionne.setChecked(true);
            }
            else{
                _toutSelectionne.setChecked(false);
            }

        }
    }

}