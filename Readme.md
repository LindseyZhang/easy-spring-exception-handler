### A plug and play Global exception handler on spring web

when throw a exception in spring web, spring always wrapper it as an
internal error with status error code 500, which is not very friendly.
 
This small util helps you to throw custom exception with given http status 
that consistent with standard exception format
but away you from boilerplate exception handling code. 

#### Main class

BusinessException: our exception class. we provide four constructor.
```$xslt
    public BusinessException(String errorCode);

    public BusinessException(String errorCode, HttpStatus httpStatus);

    public BusinessException(String errorCode, Object modelInfo);

    public BusinessException(String errorCode, HttpStatus httpStatus, Object modelInfo);
```
if HttpStatus is not provided, then the default HttpStatus is 400:BAD_REQUEST.

modelInfo allow you to put any thing that you want to throw in the exception, which will show in the exception body as an extra. 

#### How to use it
1. add dependency.  
   in gradle  
   ```$xslt
	  compile('com.github.lindseyzhang:easy-spring-exception-handler:2.0.0')
   ```
   in maven
   ```$xslt
   <dependency>
       <groupId>com.github.lindseyzhang</groupId>
       <artifactId>easy-spring-exception-handler</artifactId>
       <version>2.0.0</version>
   </dependency>
    ```
   
2. add you own errorCode enum class.

    ```$java
    public enum ErrorCode {
        PRODUCT_ALREADY_EXIST;
    }
    ```
3. throw a BusinessException in Code.
    ```$java
        public void addProduct(Product product) {
    
            Product oldProduct = productRepository.findByName(product.getName());
            if (oldProduct != null) {
                throw new BusinessException(ErrorCode.PRODUCT_ALREADY_EXIST.name(), oldProduct);
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
    
#### support for i18n
This simple library also support i18n, you can simply add messages.properties 
under resource package to use it. 

eg: with ErrorKey class
```$java
    public enum ErrorCode {
        PRODUCT_ALREADY_EXIST;
    }
```
the messages.properties may look like this.

```$xslt
PRODUCT_ALREADY_EXIST=product already exist!
```

the ErrorMessage value will be use as the key in messages.properties, 
if the key can't find in messages.properties, then the default message will be the errorMessage. 
In this case the  PRODUCT_EXCEED_VOLUME exception's error message will be product_exceed_volume.
 
the messages.properties without the locale info will use as default. 
if you want to resolve message against specific language and country, named the file like 
messages_[lang](https://en.wikipedia.org/wiki/ISO_639-1)_[countryCode](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2).properties.
For example, for chinese, add a new properties file named messages_zh_CN.properties under resource package.
[see here for more i18n information](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core)  