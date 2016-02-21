package com.chen.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: 对@RequestBody、@responseBody 注解数据进行转换
 * Author: wei
 * DateTime: 2015-08-14 12:09
 */
public class CustomMappingJackson2HttpMessageConverter extends
        MappingJackson2HttpMessageConverter {

    private static final boolean jackson23Available = ClassUtils.hasMethod(
        ObjectMapper.class, "canDeserialize", JavaType.class,
        AtomicReference.class);

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {

        if (!jackson23Available || !logger.isWarnEnabled()) {
            return (this.getObjectMapper().canSerialize(clazz) && canWrite(mediaType));
        }

        // string 特殊处理
        if (String.class.equals(clazz)) {
            return true;
        }

        AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
        if (this.getObjectMapper().canSerialize(clazz, causeRef)
                && canWrite(mediaType)) {
            return true;
        }

        Throwable cause = causeRef.get();
        if (cause != null) {
            String msg = "Failed to evaluate serialization for type [" + clazz
                    + "]";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, cause);
            } else {
                logger.warn(msg + ": " + cause);
            }
        }
        return false;
    }

    protected boolean canWrite(MediaType mediaType) {
        if (mediaType == null || MediaType.ALL.equals(mediaType)) {
            return true;
        }
        for (MediaType supportedMediaType : getSupportedMediaTypes()) {
            if (supportedMediaType.isCompatibleWith(mediaType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        JsonObject jsonObject = new JsonObject(object);
        super.writeInternal(jsonObject, outputMessage);
    }
}
