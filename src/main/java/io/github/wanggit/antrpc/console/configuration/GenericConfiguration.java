package io.github.wanggit.antrpc.console.configuration;

import io.github.wanggit.antrpc.commons.codec.cryption.ICodec;
import io.github.wanggit.antrpc.commons.codec.cryption.NoOpCodec;
import io.github.wanggit.antrpc.commons.generic.DefaultServiceProviderInvoker;
import io.github.wanggit.antrpc.commons.generic.IServiceProviderInvoker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

@Configuration
public class GenericConfiguration {

    @Value("${antrpc.service.codec.enable}")
    private boolean codecEnable;

    @Value("${antrpc.service.codec.type}")
    private String codecType;

    @Value("${antrpc.service.codec.key}")
    private String codecKey;

    @Bean
    public IServiceProviderInvoker serviceProviderInvoker()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ICodec codec = null;
        if (codecEnable) {
            Class<?> codecClazz =
                    ClassUtils.forName(codecType, GenericConfiguration.class.getClassLoader());
            codec = (ICodec) codecClazz.newInstance();
            codec.setKey(codecKey);
        }
        if (null == codec) {
            codec = new NoOpCodec();
        }
        return new DefaultServiceProviderInvoker(codec, codecKey, null);
    }
}
