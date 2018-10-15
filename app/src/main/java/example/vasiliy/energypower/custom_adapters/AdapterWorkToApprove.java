package example.vasiliy.energypower.custom_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import example.vasiliy.energypower.R;
import example.vasiliy.energypower.WorkManager;
import example.vasiliy.energypower.model.WR_Table;

import static android.support.constraint.Constraints.TAG;

public class AdapterWorkToApprove extends ArrayAdapter<WR_Table> implements View.OnCreateContextMenuListener{
    private ArrayList<WR_Table> data;
    private Context mContext;

    private final String TAG = WorkManager.class.getSimpleName();

    private static class ViewHolder{
        TextView txtEmployee;
        TextView txtWorkType;
        TextView txtNumDay;
        TextView txtWork;
        TextView txtOverWork;
        CheckBox cbApprove;
    }

    public AdapterWorkToApprove(ArrayList<WR_Table> arrayList, Context context){
        super(context, R.layout.work_approve_item);
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
    public WR_Table getItem(int position) {
        return data.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WR_Table item = getItem(position);
        final ViewHolder viewHolder;

        View v = convertView;

        if(v == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.work_approve_item,parent,false);
            viewHolder.txtEmployee = v.findViewById(R.id.txtItemEmployee);
            viewHolder.txtWorkType = v.findViewById(R.id.txtItemWorkType);
            viewHolder.txtNumDay = v.findViewById(R.id.txtItemNumDay);
            viewHolder.txtWork = v.findViewById(R.id.txtItemWorkTime);
            viewHolder.txtOverWork = v.findViewById(R.id.txtItemOverWork);
            viewHolder.cbApprove = v.findViewById(R.id.cbApprove);

            viewHolder.cbApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    WR_Table element = (WR_Table)viewHolder.cbApprove.getTag();
                    element.setSelected(compoundButton.isChecked());
                    Log.e(TAG, "нажато" + element.getEmployee() + " " + element.getNumDay() + " " + element.isSelected());
                }
            });
            v.setTag(viewHolder);
            viewHolder.cbApprove.setTag(data.get(position));
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.txtEmployee.setText(item.getEmployee().toString());
        viewHolder.txtWorkType.setText(item.getWorkType().toString());
        viewHolder.txtNumDay.setText(String.valueOf(item.getNumDay()));
        viewHolder.txtWork.setText(String.valueOf(item.getWorkHours()));
        viewHolder.txtOverWork.setText(String.valueOf(item.getOverWorkHours()));
        viewHolder.cbApprove.setChecked(item.isSelected());

        v.setOnCreateContextMenuListener(this);
        viewHolder.txtEmployee.setTag(position);

        return v;

    }
}
