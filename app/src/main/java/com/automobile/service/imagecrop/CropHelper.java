package com.automobile.service.imagecrop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class CropHelper {

    public static final String TAG = "CropHelper";
    public static File uriFile =null;

    /**
     * request code of Activities or Fragments
     * You will have to change the values of the request codes below if they conflict with your own.
     */
    public static final int REQUEST_CROP = 127;
    public static final int REQUEST_CAMERA = 128;
    public static final int REQUEST_PICK = 129;

    public static final String CROP_CACHE_FOLDER = "PhotoCropper";
    private static Activity activity;

    public static Uri generateUri()
    {
        File tempDir = new File(Environment.getExternalStorageDirectory() + File.separator + CROP_CACHE_FOLDER);
       // File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
       // File tempDir = new File(sdCard.getAbsolutePath() + "/"+CROP_CACHE_FOLDER);

        if (!tempDir.exists()) {
            try {
                boolean result = tempDir.mkdir();
            } catch (Exception e) {
                Log.e(TAG, "generateUri failed: " + tempDir, e);
            }
        }
        String name = "app"+System.currentTimeMillis();

        try {
            uriFile = File.createTempFile(name, ".jpg", tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(uriFile);

        //String name = String.format("image-%d.jpg", System.currentTimeMillis());
       // return Uri.fromFile(tempDir).buildUpon().appendPath(name).build();
    }

    public static boolean isPhotoReallyCropped(Uri uri) {
        File file = new File(uri.getPath());
        long length = file.length();
        return length > 0;
    }

    public static void handleResult(CropHandler handler, int requestCode, int resultCode, Intent data) {
        if (handler == null) return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCancel();
        } else if (resultCode == Activity.RESULT_OK) {
            CropParams cropParams = handler.getCropParams();
            if (cropParams == null) {
                handler.onFailed("CropHandler's params MUST NOT be null!");
                return;
            }
            switch (requestCode) {
                case REQUEST_PICK:
                case REQUEST_CROP:
                    if (isPhotoReallyCropped(cropParams.uri)) {
                        Log.d(TAG, "Photo cropped!");
                        onPhotoCropped(handler, cropParams);
                        break;
                    } else {
                        Context context = handler.getCropParams().context;
                        if (context != null) {
                            if (data != null && data.getData() != null) {
                                String path = CropFileUtils.getSmartFilePath(context, data.getData());
                                boolean result = CropFileUtils.copyFile(path, cropParams.uri.getPath());
                                if (!result) {
                                    handler.onFailed("Copy file to cached folder failed");
                                    break;
                                }
                            } else {
                                handler.onFailed("Returned data is null " + data);
                                break;
                            }
                        } else {
                            handler.onFailed("CropHandler's context MUST NOT be null!");
                        }
                    }
                case REQUEST_CAMERA:
                    if (cropParams.enable) {
                        // Send this Uri to Crop
                        Intent intent = buildCropFromUriIntent(cropParams);
                        handler.handleIntent(intent, REQUEST_CROP);
                    } else {
                        Log.d(TAG, "Photo cropped!");
                        onPhotoCropped(handler, cropParams);
                    }
                    break;
            }
        }
    }

    private static void onPhotoCropped(CropHandler handler, CropParams cropParams) {
        if (cropParams.compress) {
            Uri originUri = cropParams.uri;
            Uri compressUri = CropHelper.generateUri();
            CompressImageUtils.compressImageFile(cropParams, originUri, compressUri);
            handler.onCompressed(compressUri);
        } else {
            //handler.onPhotoCropped(cropParams.uri);
            handler.onPhotoCropped(Uri.fromFile(uriFile));
        }
    }

    // None-Crop Intents

    public static Intent buildGalleryIntent(CropParams params, Activity mActivity) {
        Intent intent;
        activity=mActivity;
        if (params.enable) {
            intent = buildCropIntent(Intent.ACTION_GET_CONTENT, params);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*");
//                    .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //File imagePath = new File(String.valueOf(params.uri));
                Uri contentUri = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".provider", uriFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
            }
        }
        return intent;
    }

    public static Intent buildCameraIntent(CropParams params, Activity mActivity)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity=mActivity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //File imagePath = new File(String.valueOf(params.uri));
            Uri contentUri = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".provider", uriFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
        }

        return intent;
        //return new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
    }

    // Crop Intents
    private static Intent buildCropFromUriIntent(CropParams params) {
        return buildCropIntent("com.android.camera.action.CROP", params);
    }

    private static Intent buildCropIntent(String action, CropParams params) {
        Intent intent= new Intent(action)
                .putExtra("crop", "true")
                .putExtra("scale", params.scale)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat)
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded);
//                .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              intent.setDataAndType(params.uri, params.type);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //File imagePath = new File(String.valueOf(params.uri));
            Uri contentUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(String.valueOf(params.uri)));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.setDataAndType(params.uri, params.type);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri);
        }


        return intent;

    }

    // Clear Cache

    public static boolean clearCacheDir() {
        File cacheFolder = new File(Environment.getExternalStorageDirectory() + File.separator + CROP_CACHE_FOLDER);
        if (cacheFolder.exists() && cacheFolder.listFiles() != null) {
            for (File file : cacheFolder.listFiles()) {
                boolean result = file.delete();
                Log.d(TAG, "Delete " + file.getAbsolutePath() + (result ? " succeeded" : " failed"));
            }
            return true;
        }
        return false;
    }

    public static boolean clearCachedCropFile(Uri uri) {
        if (uri == null) return false;

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            Log.d(TAG, "Delete " + file.getAbsolutePath() + (result ? " succeeded" : " failed"));
            return result;
        }
        return false;
    }
}
