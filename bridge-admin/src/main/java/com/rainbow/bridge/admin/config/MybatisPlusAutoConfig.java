package com.rainbow.bridge.admin.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * mybatis plus 配置器
 * @author yunlong.liu
 * @date 2020-09-29 11:10:20
 */

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(MybatisPlusProperties.class)
@AutoConfigureBefore(com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration.class)
@ConditionalOnClass(value = {PaginationInnerInterceptor.class})
public class MybatisPlusAutoConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(1000L);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setOptimizeJoin(true);
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        // 指定数据库类型为mysql，提高分页查询效率
        paginationInterceptor.setDbType(DbType.MYSQL);

        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;

    }
}
