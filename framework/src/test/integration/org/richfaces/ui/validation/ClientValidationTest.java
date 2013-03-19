package org.richfaces.ui.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.deployment.FrameworkDeployment;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;
import org.richfaces.ui.common.Bean;

@RunWith(Arquillian.class)
@RunAsClient
public class ClientValidationTest extends ValidationTestBase {

    @Deployment
    public static WebArchive deployment() {
        FrameworkDeployment deployment = new FrameworkDeployment(ClientValidationTest.class);

        deployment.archive().addClasses(Bean.class);

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void testSubmitTooShortValue() throws Exception {
        submitValueAndCheckMessage("", not(equalTo("")));
    }

    @Test
    public void testSubmitTooLongValue() throws Exception {
        submitValueAndCheckMessage("123456", not(equalTo("")));
    }

    @Test
    public void testSubmitProperValue() throws Exception {
        submitValueAndCheckMessage("ab", equalTo(""));
    }

    private static void addIndexPage(org.richfaces.deployment.Deployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <h:inputText id='text' value='#{test.value}'>");
        p.body("        <f:validateLength minimum='1' maximum='3' />");
        p.body("        <r:validator event='blur' />");
        p.body("    </h:inputText>");
        p.body("    <h:outputText id='out' value='#{test.value}'></h:outputText>");
        p.body("</h:form>");

        p.body("<r:message id='uiMessage' for='text' />");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
