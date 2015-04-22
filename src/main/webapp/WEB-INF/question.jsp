<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <sj:head locale="en-US" jqueryui="true" jquerytheme="redmond" />
        <title>Natural language interface for query answering</title>
    </head>
    <body>
        <sj:accordion id="accordion" heightStyle="content" animate="true">
            <sj:accordionItem title="Question" >
                <s:form action="question" namespace="/" >
                    <sj:textarea name="question" rows="2" cols="100" />
                    <sj:submit />
                </s:form>
            </sj:accordionItem>
            <sj:accordionItem title="Step 1">
                <pre>
<s:property value="step1Output" />
                </pre>
            </sj:accordionItem>
            <sj:accordionItem title="Step 2">
                <pre>
<s:property value="step2Output" />        
                </pre>
            </sj:accordionItem>
            <sj:accordionItem title="Step 3">
                <pre>
<s:property value="step3Output" />        
                </pre>
            </sj:accordionItem>
            <sj:accordionItem title="Answer">
<s:property value="answer" />        
            </sj:accordionItem>            
        </sj:accordion>
    </body>
</html>
