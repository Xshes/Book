package com.example.book;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.book.utils.SelectPicPopupWindow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * BaseActivity<br>
 */
public class BaseActivity extends FragmentActivity {
	
	/**
	 * 选择图片的返回码
	 */
	public final static int SELECT_IMAGE_RESULT_CODE = 200; 
	/**
	 * 当前选择的图片的路径
	 */
	public String mImagePath;
	/**
	 * 自定义的PopupWindow
	 */
	private SelectPicPopupWindow menuWindow;
	/**
	 * Fragment回调接口
	 */
	public OnFragmentResult mOnFragmentResult;
	
	public void setOnFragmentResult(OnFragmentResult onFragmentResult){
		mOnFragmentResult = onFragmentResult;
	}
	
	/**
	 * 回调数据给Fragment的接口
	 */
	public interface OnFragmentResult{
		void onResult(String mImagePath);
	}
	
	/**
	 * 拍照或从图库选择图片(Dialog形式)
	 */
	public void showPictureDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[] { "拍摄照片", "选择照片", "取消" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int position) {
						switch (position) {
						case 0://拍照  
							takePhoto();
							break;
						case 1://相册选择图片
							pickPhoto();
							break;
						case 2://取消
							break;
						default:
							break;
						}
					}
				});
		builder.create().show();
	}
	
	/**
	 * 拍照或从图库选择图片(PopupWindow形式)
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			//就像onActivityResult一样这个地方就是判断你是从哪来的。
			case 222:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission Granted
					takePhoto();
				} else {
					// Permission Denied
					Toast.makeText(BaseActivity.this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
	public void showPicturePopupWindow(){
		menuWindow = new SelectPicPopupWindow(this, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 隐藏弹出窗口
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.takePhotoBtn:// 拍照
					if (Build.VERSION.SDK_INT >= 23) {
						int checkCallPhonePermission = ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.CAMERA);
						if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
							ActivityCompat.requestPermissions(BaseActivity.this,new String[]{Manifest.permission.CAMERA},222);
							return;
						}else{

							takePhoto();//调用具体方法
						}
					} else {

						takePhoto();//调用具体方法
					}

					break;
				case R.id.pickPhotoBtn:// 相册选择图片
					pickPhoto();
					break;
				case R.id.cancelBtn:// 取消
					break;
				default:
					break;
				}
			}
		});  
		menuWindow.showAtLocation(findViewById(R.id.choose_layout), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在

		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			/**
			 * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
			 */
			//获取与应用相关联的路径
			String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
			//根据当前时间生成图片的名称
			String timestamp = "/"+formatter.format(new Date())+".jpg";
			File imageFile = new File(imageFilePath,timestamp);// 通过路径创建保存文件
			mImagePath = imageFile.getAbsolutePath();
			Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
			startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE);
		} else {
			Toast.makeText(this, "内存卡不存在!", Toast.LENGTH_LONG).show();
		}
	}
	/***
	 * 从相册中取图片
	 */
	private void pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, SELECT_IMAGE_RESULT_CODE);
	}

}
