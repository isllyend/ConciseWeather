package isllyend.top.conciseweather.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Chigo on 2017/2/20.
 * Email:isllyend@gmail.com
 */

/**
 * 图表工具类
 */
public class ChartUtil {
    private static List<PointValue> mPointValues = new ArrayList<PointValue>();
    private static List<PointValue> mPointValues1 = new ArrayList<PointValue>();
    private static List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private static List<AxisValue> axisValuesY = new ArrayList<AxisValue>();

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables(List<String> date){
        for (int i = 0; i < date.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel((date.get(i))));
        }
    }
    /**
     * 图表的每个点的显示
     */
    public static void getAxisPoints(List<String> temp,List<String> temp2) {
        mPointValues.clear();
        mPointValues1.clear();
        for (int i = 0; i < temp.size(); i++) {
            mPointValues.add(new PointValue(i, Integer.parseInt(temp.get(i))));
            mPointValues1.add(new PointValue(i,Integer.parseInt(temp2.get(i))));
        }
    }
    public static void initLineChart(LineChartView lineChart){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#88FFFFFF"));  //
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setStrokeWidth(2);
        line.setPointRadius(3);
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）


        Line line1 = new Line(mPointValues1).setColor(Color.parseColor("#88FFFFFF"));  //折线的颜色（橙色）
        line1.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line1.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line1.setFilled(false);//是否填充曲线的面积
        line1.setStrokeWidth(2);
        line1.setPointRadius(3);
        line1.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line1.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line1.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

        lines.add(line);
        lines.add(line1);

        LineChartData data = new LineChartData();
        data.setValueLabelTextSize(15);// 设置数据文字大小
        data.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
        data.setValueLabelBackgroundAuto(false);
        data.setValueLabelBackgroundEnabled(true);
        data.setValueLabelBackgroundColor(Color.TRANSPARENT);

//        data.setBaseValue(199);
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
//        axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
//        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
//        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
//        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限()
        Axis axisY = new Axis();  //Y轴
//        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisX.setValues(new ArrayList<AxisValue>());
//        axisY.setValues(mAxisXValues);
        axisY.setHasLines(true);
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边




        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(false);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setVisibility(View.VISIBLE);


        lineChart.setLineChartData(data);

        final Viewport v = new Viewport(lineChart.getMaximumViewport());
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);



    }
}
