package com.cargolinklabs.loadmatch.platform.shared.infrastructure.i18n.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Configures locale resolution for REST requests
 * <p>
 *     Supports English (en_US) and Latin American Spanish (es_419), with English as the default language.
 *     Any Spanish variant sent in the Accept-Language header (es, es-PE, es-419) resolves to es_419.
 * </p>
 */
@Configuration
public class LocaleConfiguration {
    public static final Locale ENGLISH_US = Locale.forLanguageTag("en-US");
    public static final Locale LATIN_AMERICAN_SPANISH = Locale.forLanguageTag("es-419");

    @Bean
    public LocaleResolver localeResolver() {
        var resolver = new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                if (request.getHeader("Accept-Language") == null) {
                    return getDefaultLocale();
                }
                for (var requestedLocale : Collections.list(request.getLocales())) {
                    for (var supportedLocale : getSupportedLocales()) {
                        if (supportedLocale.getLanguage().equals(requestedLocale.getLanguage())) {
                            return supportedLocale;
                        }
                    }
                }
                return getDefaultLocale();
            }
        };
        resolver.setDefaultLocale(ENGLISH_US);
        resolver.setSupportedLocales(List.of(ENGLISH_US, LATIN_AMERICAN_SPANISH));
        return resolver;
    }
}
