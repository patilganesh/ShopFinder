package com.gajananmotors.shopfinder.helper;


public class Constant{

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static final String PROVIDER_URL = "http//control.c2sms.com/api/sendhttp.php";
    public static final String QUESTION_PARAMETER = "?";
    public static final String EQUALS_TO_PARAMETER = "=";
    public static final String AMPERSAND_PARAMETER = "&";
    public static final String AUTHKEY_PARAMETER = "authkey";
    public static final String MOBILES_PARAMETER = "mobiles";
    public static final String MESSAGE_PARAMETER = "message";
    public static final String SENDER_PARAMETER = "sender";
    public static final String ROUTE_PARAMETER = "route";
    public static final String SENDER = "TR";
    public static final String ROUTE = "4";
    public static final String AUTHKEY = "119718AkintvzJaWd578dd8d3";
    public static final String MESSAGE = "Hi, Your otp is:";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static boolean doubleBackToExitPressedOnce = false;





    /*@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.CUR_DEVELOPMENT)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);

                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
*/


}