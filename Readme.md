### A plug and play Global exception handler on spring web

when throw a exception in spring web, spring always wrapper it as an
internal error with status error code 500, which is not very friendly.
 
This small util helps you to throw custom exception with given http status 
that consistent with standard exception format
but away you from boilerplate exception handling code. 

#### Main class

ErrorCode: error code interface, which you should implement to define your custom error code.
 
BusinessException: our exception class. we provide four constructor.
```$xslt
    public BusinessException(ErrorCode errorCode);

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus);

    public BusinessException(ErrorCode errorCode, Object modelInfo);

    public BusinessException(ErrorCode errorCode, HttpStatus httpStatus, Object modelInfo);
```
if HttpStatus is not provided, then the default HttpStatus is 400:BAD_REQUEST.

modelInfo allow you to put any thing that you want to throw in the exception, which will show in the exception body as an extra. 

#### How to use it
1. add dependency  
   find the pre build jar in build/libs or pull this project and build it locally. run
   ```$xslt
   ./gradlew build
   ```
   then find the jar file under path build/libs/global-exception-handler-0.0.1.jar.
   
   copy this jar file to libs/ folder(if not exist then create one) directly under your project's root path.
   add dependency in gradle with
   ```$xslt
    implementation  files('libs/global-exception-handler-0.0.1.jar')
   ```
   
2. add you own custom error class, which should implement ErrorCode interface and override the getErrorMessage function,
which will be used as the message return in response body when an exception occured.

    ```$java
    import com.zlp.util.exception.ErrorCode;

    public enum ErrorKey implements ErrorCode {
        PRODUCT_ALREADY_EXIST("product already exist.");

        private String errorMessage;

        ErrorKey(String errorMsg) {
            this.errorMessage = errorMsg;
        }

        @Override
        public String getErrorMessage() {
          return errorMessage;
        }
    }
    ```
3. throw a BusinessException in Code.
    ```$java
        public void addProduct(Product product) {
    
            Product oldProduct = productRepository.findByName(product.getName());
            if (oldProduct != null) {
                throw new BusinessException(ErrorKey.PRODUCT_ALREADY_EXIST, oldProduct);
            }
            productRepository.save(product);
        }
    ```
4. make spring scan this handler by config the @ComponentScan
    ```$java
    @SpringBootApplication
    @ComponentScan({"com.test.demo", "com.zlp.util.exception"})
    public class Application {
    
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    
    }
    ```
    in @ComponentScan, you should add this exception handler's package to the scan path, which is 
    com.zlp.util.exception, and the application's root package, in my case is com.test.demo 
    or any other your scan path. 