package com.hellobike.riskControl.drools;

import com.hellobike.utils.GwDataStruct;
import com.hellobike.utils.GwDataStructService;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
//import org.kie.internal.KnowledgeBase;
//import org.kie.internal.KnowledgeBaseFactory;

import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;


import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.io.UnsupportedEncodingException;
import java.sql.*;


public class DroolsUtil {
//    static StatefulKnowledgeSession kieSession;
    static KieSession kieSessions;
    static KieContainer kieContainer;
    static KieBase kieBase;

    public DroolsUtil() throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
        Class.forName("org.postgresql.Driver");
//        Connection connection= DriverManager.getConnection("jdbc:postgresql://10.111.40.148:5432/ticket","dbuser", "dbuser@cys");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ticket", "dbuser", "dbuser@cys");

        String sql = "select * from my_rule";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String rule = null;
        while (resultSet.next()) {
            rule = resultSet.getString("rule");
            System.out.println(rule);
        }
        statement.close();
        connection.close();
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kb.getErrors();
        for (KnowledgeBuilderError error : errors) {
            System.out.println(error);
        }
        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());

        kieSessions = kBase.newKieSession();
    }

    public static KieSession getKieSession() throws  ClassNotFoundException, SQLException, UnsupportedEncodingException {
        if(kieSessions == null){
            new DroolsUtil();
        }
        return kieSessions;
    }

    public static KieSession getKieSessionDynamic() throws  ClassNotFoundException, SQLException, UnsupportedEncodingException{
        Class.forName("org.postgresql.Driver");
        Connection connection= DriverManager.getConnection("jdbc:postgresql://10.111.40.148:5432/ticket","dbuser", "dbuser@cys");
//        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ticket", "dbuser", "dbuser@cys");
//            System.out.println("是否成功连接pg数据库"+connection);
        String sql = "select * from my_rule";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String rule = null;
        while (resultSet.next()) {
            rule = resultSet.getString("rule");
//            System.out.println(rule);
        }
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kb.getErrors();
        for (KnowledgeBuilderError error : errors) {
            System.out.println(error);
        }
        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());
        statement.close();
        connection.close();
        kieSessions = kBase.newKieSession();

        return kieSessions;
    }



    public static boolean initKieSession(String newRule) {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kfs = kieServices.newKieFileSystem();
//        kfs.write("src/main/resources/rules/rules.drl", DroolsUtil_Dynamic.RULE.getBytes());
        if(!newRule.equals(RULE)) {
            RULE = newRule;
            kfs.write("src/main/resources/rules/rules.drl", newRule.getBytes());
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            Results results = kieBuilder.getResults();
            if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                System.out.println(results.getMessages());
                System.out.println("-----------语法解析报错，drools规则更新无效------------------");
                return false;
            }
            KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            KieBase kieBase = kieContainer.getKieBase();
            kieSessions = kieBase.newKieSession();
            return true;
        }
        else {
            return false;
        }
    }

    public static String RULE = "";

    public static void updateRule(){
        try {
            String newRule = getNewRule();
            if (newRule != null && newRule.length() != 0 && (!newRule.equals(RULE))) {
                boolean isGoodRule=initKieSession(newRule);
                if(isGoodRule){
                    GwDataStructService gwDataStructService = new GwDataStructService();

                    try{
                        Class global = kieSessions.getGlobal("gwDataStructService").getClass();
                        System.out.println(global);
                    }
                    catch (Exception e){
                        kieSessions.setGlobal("gwDataStructService", gwDataStructService);
                    }

                    System.out.println("RULE被成功更新");
                    System.out.println("session被成功更新创建");
                }
            }
            System.out.println(kieSessions);
//            return kieSessions;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return kieSessions;
    }

    public static String getNewRule() throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
        Class.forName("org.postgresql.Driver");
        Connection connection= DriverManager.getConnection("jdbc:postgresql://116.62.120.210:5432/ticket","dbuser", "dbuser@cys");
//        Connection connection= DriverManager.getConnection("jdbc:postgresql://10.111.40.148:5432/ticket","dbuser", "dbuser@cys");
//        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ticket", "dbuser", "dbuser@cys");
//            System.out.println("是否成功连接pg数据库"+connection);
        String sql = "select * from my_rule";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        String rule = null;
        while (resultSet.next()) {
            rule = resultSet.getString("rule");
//            System.out.println(rule);
        }
        statement.close();
        connection.close();
        return rule;
    }

    public static void execRule(GwDataStruct gwDataStruct){

        FactHandle ss = kieSessions.insert(gwDataStruct);
        kieSessions.fireAllRules();
        kieSessions.delete(ss);
    }


    public static void main(String[] args) throws Exception {

        while (true){
            DroolsUtil.updateRule();
            Thread.sleep(1000*6);
        }
    }

}
