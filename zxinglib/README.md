## zxinglib

### 使用方法
* 根据字符串生成二维码图片
        /**
          * 创建二维码
          *
          * @param content   content   需要生产二维码的内容
          * @param widthPix  widthPix  二维码宽度
          * @param heightPix heightPix 二维码高度
          * @param logoBm    logoBm    二维码中间logo（BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo)）
          * @return 二维码
          */
      private void createCodeImage(Context context,String content, int widthPix, int heightPix, Bitmap logoBm) {
         if (!content.equals("")) {
             //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
             Bitmap qrCodeBitmap = EncodingUtils.createQRCode(
                     content, widthPix, heightPix, logoBm);
             //设置二维码中间的团logo
             Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.app_logo);
             EncodingUtils.addLogo(bitmap, bitmap);
             codeImageview.setImageBitmap(qrCodeBitmap);
         } else {
             Toast.makeText(context, "Text can not be empty", Toast.LENGTH_SHORT).show();
         }
      }
* 根据字符串生成条形码图片
      private void crateCodeBar(Context context,String content,int width,int heigt) {
             Bitmap bitmap=null;
             for(int i=0;i<content.length();i++){
                 int c=content.charAt(i);
                 if ((19968 <= c && c < 40623)) {
                     Toast.makeText(context, "生成条形码的内容不能是中文", Toast.LENGTH_SHORT).show();
                     return;
                 }
             }
             if(content!=null&&!"".equals(content)){
                 bitmap = EncodingUtils.creatBarcode(context, content, width, heigt, true);
             }
             if(bitmap!=null){
                 codebarImageView.setImageBitmap(bitmap);
             }
         }
* 扫一扫功能
      1、首先要动态申请权限：Manifest.permission.CAMERA
      2、Intent intent = new Intent(getContext(), CaptureActivity.class);
         startActivityForResult(intent, SCANSCAN_CODE);
      3、 @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (data == null) {
              return;
            }
            if (requestCode == SCANSCAN_CODE && resultCode == RESULT_OK) {
              Bundle extras = data.getExtras();
              String result = (String) extras.get("result");//即为扫描的结果
              toastShow(result);
            }
          }