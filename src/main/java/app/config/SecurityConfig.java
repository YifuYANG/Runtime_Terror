package app.config;

import app.repository.UserRepository;
import org.jasypt.util.numeric.StrongIntegerNumberEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public StrongTextEncryptor ppsnEncoder() {
        StrongTextEncryptor ppsnEncrypt = new StrongTextEncryptor();
        ppsnEncrypt.setPassword("MyPPSNencryption");
        return ppsnEncrypt;
    }

    @Bean
    public StrongTextEncryptor dobEncoder() {
        StrongTextEncryptor dobEncrypt = new StrongTextEncryptor();
        dobEncrypt.setPassword("MyDobEncryption");
        return dobEncrypt;
    }

    @Bean
    public StrongIntegerNumberEncryptor phoneNumberEncoder() {
        StrongIntegerNumberEncryptor phoneNumberEncrypt = new StrongIntegerNumberEncryptor();
        phoneNumberEncrypt.setPassword("MyPhoneNumberEncryption");
        return phoneNumberEncrypt;
    }
}