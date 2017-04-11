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

import tech.linard.miniolimpiadasditec.Modalidade;
import tech.linard.miniolimpiadasditec.R;

/**
 * Created by lucas on 08/04/17.
 */

class ModalidadeAdapter extends ArrayAdapter<Modalidade> {

    public ModalidadeAdapter(@NonNull Context context,
                             @LayoutRes int resource,
                             @NonNull List<Modalidade> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater()
                    .inflate(R.layout.modalidades_list_item, parent, false);
        }




        TextView txtModalidade = (TextView) convertView.findViewById(R.id.txt_modalidade);
        Modalidade modalidade = getItem(position);
        txtModalidade.setText(modalidade.getName());

        return convertView;
    }
}
