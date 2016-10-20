/**
 *
 */
package com.kz.android.core;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.kz.android.bean.HttpBaseParams;
import com.kz.android.bean.HttpBean;
import com.kz.android.bean.HttpHeaderBean;
import com.kz.android.bean.HttpParamsBean;
import com.kz.android.special.AppServer;
import com.kz.android.util.KLog;
import com.kz.android.util.KNetwork;
import com.kz.android.util.KToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http服务类 负责加载接口和加载图片以及上传图片的网络操作
 *
 * @author 作者 E-mail:柯壮 15011033790
 * @since 创建时间：2016年3月28日 上午10:02:14
 */
public class HttpServer implements AppServer {
    private OkHttpClient mHttpClient;
    private Context mContext;
    private Activity mActivity;
    private static final String NO_AVAILABLE_NET = "网络无效";
    private static final String THREAD_NAME = HttpServer.class.getSimpleName();
    private static final long TIME_OUT = 30;
    private static final long READ_TIME_OUT = TIME_OUT;

    HttpServer() {
    }

    @Override
    public void initServer(Context context) {
        mContext = context;
        mHttpClient = new OkHttpClient().newBuilder().connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();
        HandlerThread mHandlerThread = new HandlerThread(THREAD_NAME);
        mHandlerThread.start();
    }

    /**
     * HTTP请求类
     *
     * @author 作者 E-mail:柯壮 15011033790
     * @since 创建时间：2016年3月28日 上午10:02:14
     */
    public class HttpLoader {
        private Dialog dialog;
        private String mUrl;
        private String mStr;

        private HttpLoader(Dialog dialog) {
            this.dialog = dialog;
        }

        /**
         * GET请求方式
         *
         * @author 作者:柯壮
         * @since 创建时间：2016年4月1日 下午3:37:13
         */
        public void get(String url, List<HttpBean> params, HttpResponse callback) {
            mUrl = url;

            //如果有参数。就处理参数拼接到url后边
            if (params != null && !params.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                StringBuilder paramsTemp = new StringBuilder();
                sb.append(url).append("?");
                for (HttpBean bean : params) {
                    if (bean instanceof HttpHeaderBean) {
                        continue;
                    }
                    sb.append(bean.key).append("=").append(bean.value).append("&");
                    if (bean instanceof HttpBaseParams) {
                        continue;
                    }
                    paramsTemp.append(bean.key).append("=").append(bean.value).append("&");
                }
                sb.deleteCharAt(sb.length() - 1);
                paramsTemp.deleteCharAt(paramsTemp.length() - 1);
                url = sb.toString();
                mStr = paramsTemp.toString();
            }

            URL u = null;
            try {
                u = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            get(u, params, callback);
        }


        /**
         * POST请求方式
         *
         * @author 作者:柯壮
         * @since 创建时间：2016年4月1日 下午3:37:13
         */
        public void post(String url, List<HttpBean> list, HttpResponse callback) {
            mUrl = url;
            URL u = null;
            try {
                u = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            post(u, list, callback);
        }

        /**
         * GET请求方式
         *
         * @author 作者:柯壮
         * @since 创建时间：2016年4月1日 下午3:37:13
         */
        void get(URL url, List<HttpBean> list, HttpResponse callback) {
            Request.Builder request = new Request.Builder().url(url);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof HttpHeaderBean) {
                        request.addHeader(list.get(i).key, list.get(i).value);
                    }
                }
            }
            performInterface(request.build(), callback);
        }

        /**
         * POST请求方式
         *
         * @author 作者:柯壮
         * @since 创建时间：2016年4月1日 下午3:37:13
         */
        void post(URL url, List<HttpBean> list, HttpResponse callback) {
            FormBody.Builder builder = new FormBody.Builder();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            StringBuilder sb = new StringBuilder();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) instanceof HttpParamsBean) {
                        builder.add(list.get(i).key, TextUtils.isEmpty(list.get(i).value) ? "" : list.get(i).value);
                        sb.append(list.get(i).key).append("=").append(list.get(i).value).append("&");
                    }
                    if (list.get(i) instanceof HttpBaseParams) {
                        builder.add(list.get(i).key, TextUtils.isEmpty(list.get(i).value) ? "" : list.get(i).value);
                    }
                    if (list.get(i) instanceof HttpHeaderBean) {
                        requestBuilder.addHeader(list.get(i).key, list.get(i).value);
                    }
                }
            }
            if (sb.length() > 0) {
                mStr = sb.deleteCharAt(sb.length() - 1).toString();
            }
            requestBuilder.post(builder.build());
            Request request = requestBuilder.build();
            performInterface(request, callback);
        }

        // 执行网络请求
        private void performInterface(Request request, final HttpResponse response) {
            //处理网络
            if (!vaildNetWork()) {
                KToast.releaseToast(mActivity, "网络不佳");
                return;
            }
            //防止内存泄漏
            try {
                if (!mActivity.isFinishing() && dialog != null) {
                    dialog.show();
                }
            } catch (Exception e) {
                return;
            }
            //开始执行网络请求部分
            KLog.i(mActivity.getClass().getSimpleName(), "请求地址:" + mUrl);
            KLog.i(mActivity.getClass().getSimpleName(), "请求参数:" + mStr);
            mHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(final Call arg0, final Response arg1) throws IOException {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    int code = arg1.code();
                    String json = Pattern.compile("\t|\r|\n").matcher(arg1.body().string()).replaceAll("");
                    //php用的Unicode编码输出的文字，所以要解码一下
                    KLog.i(mActivity.getClass().getSimpleName(), "响应结果:" + json);
                    final int finalCode = code;
                    final String finalJson = json;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalCode == 200) {
                                response.onSucceed(finalCode, finalJson);
                            } else {
                                response.onFailed(finalCode, "请求失败");
                            }

                        }
                    });
                }

                @Override
                public void onFailure(final Call arg0, final IOException arg1) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            response.onFailed(-1, "网络超时");
                        }
                    });
                }
            });
        }
    }

    /**
     * 获取接口加载器
     *
     * @author 作者:柯壮
     * @since 创建时间：2016年4月6日 下午3:37:34
     */
    public HttpLoader http(Dialog dialog, Activity activity) {
        this.mActivity = activity;
        return new HttpLoader(dialog);

    }

    // 检测当前网络
    private boolean vaildNetWork() {
        if (!KNetwork.isAvailable(mContext)) {
            KToast.releaseToast(mContext, NO_AVAILABLE_NET);
            return false;
        }
        return true;
    }

    /**
     * 负责接口相应接口
     *
     * @author 作者 E-mail:柯壮 15011033790
     * @since 创建时间：2016年3月28日 上午10:02:14
     */
    public interface HttpResponse {
        /**
         * 接口的回调方法，成功请求
         *
         * @since 创建时间：2016年3月31日 下午4:51:12
         */
        void onSucceed(int code, String json);

        /**
         * 接口的回调方法，失败请求
         */
        void onFailed(int code, String error);
    }
}
