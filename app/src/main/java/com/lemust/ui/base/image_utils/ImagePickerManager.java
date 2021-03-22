package com.lemust.ui.base.image_utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.lemust.LeMustApp;
import com.lemust.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class ImagePickerManager {
    private final int TAKE_PHOTO_KEY = ChooserType.REQUEST_CAPTURE_PICTURE;
    private final int CHOOSE_PHOTO_KEY = ChooserType.REQUEST_PICK_PICTURE;

    private static ImagePickerManager imagePickerManager;
    private OnImagePickedListener onImagePickedListener;

    private ImagePickerManager() {
    }

    public static ImagePickerManager getInstance() {
        if (imagePickerManager == null) {
            imagePickerManager = new ImagePickerManager();
        }
        return imagePickerManager;
    }

    public void chooseImage(final Activity activity, final OnImagePickedListener onImagePickedListener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new io.reactivex.functions.Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    imagePickerManager.onImagePickedListener = onImagePickedListener;
                    startActivityForChoosingPhoto(activity);
                } else {
                    Toast.makeText(activity, LeMustApp.instance.getApplicationContext().getString(R.string.permission_was_not_granted), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void takeImage(final Activity activity, final OnImagePickedListener onImagePickedListener) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new io.reactivex.functions.Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    imagePickerManager.onImagePickedListener = onImagePickedListener;
                    startActivityForTakingPhoto(activity);
                } else {
                    Toast.makeText(activity, LeMustApp.instance.getApplicationContext().getString(R.string.permission_was_not_granted), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void startActivityForTakingPhoto(Activity activity) {
        Intent intent = getPickImageIntent(activity);
        intent.putExtra(ImagePickerHiddenActivity.PICK_IMAGE_FROM, TAKE_PHOTO_KEY);
        activity.startActivity(intent);
    }

    private void startActivityForChoosingPhoto(Activity activity) {
        try {

            Intent intent = getPickImageIntent(activity);
            intent.putExtra(ImagePickerHiddenActivity.PICK_IMAGE_FROM, CHOOSE_PHOTO_KEY);
            activity.startActivity(intent);
        }catch (Exception e){}
    }


    @NonNull
    private Intent getPickImageIntent(Activity activity) {
        return new Intent(activity, ImagePickerHiddenActivity.class);
    }

    public void setChosenImage(ChosenImage chosenImage) {
        if (onImagePickedListener != null) {
            onImagePickedListener.onChosenImageReady(chosenImage);
        }
        releaseInstance();
    }

    private void releaseInstance() {
        if (onImagePickedListener != null) {
            onImagePickedListener = null;
        }
        if (imagePickerManager != null) {
            imagePickerManager = null;
        }
    }


    public interface OnImagePickedListener {
        void onChosenImageReady(ChosenImage chosenImage);
    }


}
