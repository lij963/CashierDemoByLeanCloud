package com.etam.cashier.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.etam.cashier.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:  admin
 * Date:    2017/2/9.
 * Description:
 */

public class GoodsListItem extends LinearLayout {

    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_number)
    TextView tvGoodsNumber;

    public GoodsListItem(Context context) {
        super(context);
        init(context);
    }

    public GoodsListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoodsListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = View.inflate(context, R.layout.item_cashier_list_goods, this);
        ButterKnife.bind(this, rootView);
    }

    public void setGoodsName(String goodsName) {
        tvGoodsName.setText(goodsName);
    }

    public void setGoodsNumber(String goodsNumber) {
        tvGoodsNumber.setText(goodsNumber);
    }

    public void setGoodsPrice(String goodsPrice) {
        tvGoodsPrice.setText(goodsPrice);
    }

}
