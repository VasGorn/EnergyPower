package example.vasiliy.energypower.custom_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import example.vasiliy.energypower.R;
import example.vasiliy.energypower.WorkManager;
import example.vasiliy.energypower.model.WR_Table;
import example.vasiliy.energypower.model.WorkType;

public class AdapterWorkTypeProgress extends ArrayAdapter<WorkType> implements View.OnCreateContextMenuListener{
    private ArrayList<WorkType> data;
    private Context mContext;

    private final String TAG = WorkManager.class.getSimpleName();

    private static class ViewHolder{
        TextView txtWorkTypeItemPro;
        TextView txtProgressItem;
        ProgressBar pbProgressItem;
    }

    public AdapterWorkTypeProgress(ArrayList<WorkType> arrayList, Context context){
        super(context, R.layout.progress_item);
        this.data = arrayList;
        this.mContext = context;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public WorkType getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WorkType item = getItem(position);
        final ViewHolder viewHolder;

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.progress_item,parent,false);
            viewHolder.txtWorkTypeItemPro = v.findViewById(R.id.txtWorkTypeItemPro);
            viewHolder.txtProgressItem = v.findViewById(R.id.txtProgressItem);
            viewHolder.pbProgressItem = v.findViewById(R.id.pbProgressItem);

            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        Log.e(TAG, "item " + item.toString());


        viewHolder.txtWorkTypeItemPro.setText(item.getTypeName());
        viewHolder.txtProgressItem.setText(String.valueOf(item.getProgress()) + "%");
        viewHolder.pbProgressItem.setProgress(item.getProgress());

        viewHolder.txtWorkTypeItemPro.setTag(position);
        return v;

    }
}
