package tech.linard.miniolimpiadasditec.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import tech.linard.miniolimpiadasditec.R;
import tech.linard.miniolimpiadasditec.SubModalidade;

/**
 * Created by lucas on 09/04/17.
 */

public class SubModalidadeAdapter extends ArrayAdapter<SubModalidade> {


    public SubModalidadeAdapter(@NonNull Context context,
                                @LayoutRes int resource,
                                @NonNull List<SubModalidade> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater()
                    .inflate(R.layout.submodalidades_list_item, parent, false);
        }

        TextView txtModalidade = (TextView) convertView.findViewById(R.id.txt_submodalidade);
        SubModalidade subModalidade = getItem(position);
        txtModalidade.setText(subModalidade.getDescricao());

        ImageView icon = (ImageView) convertView.findViewById(R.id.icon_submodalidade);
        if (subModalidade.isFeminino()) {
            icon.setBackgroundResource(R.drawable.human_female);
        } else {
            if (subModalidade.isMasculino()) {
                icon.setBackgroundResource(R.drawable.human_male);
            } else {
                icon.setBackgroundResource(R.drawable.human_male_female);
            }
        }
        return convertView;
    }

}
