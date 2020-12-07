package asia.wemap.androidsdk.exceptions;

import androidx.annotation.NonNull;

public class WeMapConfigurationException extends RuntimeException {

  public WeMapConfigurationException() {
    super("\nĐể bắt đầu sử dụng WeMap SDK vui lòng gọi WeMap.getInstance(Context context, String accessToken) trước khi "
      + "tạo View. Access token trường bắt buộc trước khi sử dụng các dịch vụ của WeMap.");
  }

  public WeMapConfigurationException(@NonNull String message) {
    super(message);
  }
}
