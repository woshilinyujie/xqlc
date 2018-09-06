package com.rongxun.xqlc.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rongxun.xqlc.Activities.PhotoDetailActivity;
import com.rongxun.xqlc.Beans.Borrow.MaterialShowBean;
import com.rongxun.xqlc.R;
import com.rongxun.xqlc.Util.AppConstants;

import java.io.Serializable;
import java.util.List;

/**
 * PROJECT_NAME: hzjcb_android
 * PACKAGE_NAME: com.rongxun.xqlc.Adapters
 * Author: HouShengLi
 * Time: 2017/07/10 11:51
 * E-mail: 13967189624@163.com
 * Description:
 */

public class MaterialShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MaterialShowBean> datas;
    private Activity mContext;
    private LayoutInflater inflater;

    public MaterialShowAdapter(List<MaterialShowBean> datas, Activity mContext) {
        this.datas = datas;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.manage_itme_material, parent, false);

        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        MaterialShowBean bean = datas.get(position);
        Vh vh = (Vh) holder;
        Glide.with(mContext)
                .load(bean.getImageUrl())
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.repay_ment_detail)
                .error(R.mipmap.repay_ment_detail)
                .into(vh.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                intent.putExtra("p", position);
                intent.putExtra("url", (Serializable) datas);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    class Vh extends RecyclerView.ViewHolder {

        ImageView img;

        public Vh(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.project_detail_image_material_show);
        }
    }
}
