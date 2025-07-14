package finku.ukim.mk.eduai.config;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceRoutingAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional) && execution(* finku.ukim.mk.eduai..*.find*(..))")
    public void routeToRead() {
        DataSourceContextHolder.useRead();
    }

    @Before("@annotation(org.springframework.transaction.annotation.Transactional) && execution(* finku.ukim.mk.eduai..*.save*(..))")
    public void routeToWrite() {
        DataSourceContextHolder.useWrite();
    }

    @After("execution(* finku.ukim.mk.eduai..*.*(..))")
    public void clearRouting() {
        DataSourceContextHolder.clear();
    }
}
