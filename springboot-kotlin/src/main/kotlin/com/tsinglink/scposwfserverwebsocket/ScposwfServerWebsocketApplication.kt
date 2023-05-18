package com.tsinglink.scposwfserverwebsocket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration


@Configuration
@SpringBootApplication(exclude = [
        LiquibaseAutoConfiguration::class,
        DataSourceAutoConfiguration::class,
        ValidationAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        JpaRepositoriesAutoConfiguration::class,
        DataSourceTransactionManagerAutoConfiguration::class
    ]
)
class ScposwfServerWebsocketApplication

fun main(args: Array<String>) {

    val runApplication = runApplication<ScposwfServerWebsocketApplication>(*args)

}
