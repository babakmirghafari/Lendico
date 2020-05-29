package com.lendico.plangenerator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class GeneratePlanControllerTests extends AbstractTestClass{

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * check if user send RequestObject in request body ,RestEndPoint should return 200
     * and if user don't send RequestObject in request body  ,RestEndPoint should return 400
     * @throws Exception
     */
    @Test
    public void testSendRequestObject() throws Exception{

        /**
         * check 200 response
         */
        mockMvc.perform(post(requestUrl).contentType(MediaType.APPLICATION_JSON)
                .content(writeObjectAsJasonString(requestObject)))
                .andExpect(status().isOk());
        /**
         * check 400 response
         */
        mockMvc.perform(post(requestUrl).contentType(MediaType.APPLICATION_JSON)
                .content(writeObjectAsJasonString(null)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Check when user send a RequestObject with initial data , RestEndPoint return ResponseObject with initiated value,
     * don't care about calculated data validation ,data validity check in service Test class
     * @throws Exception
     */
    @Test
    public void checkResponse() throws Exception {
        mockMvc.perform(post(requestUrl).contentType(MediaType.APPLICATION_JSON)
                .content(writeObjectAsJasonString(requestObject)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                /**
                 * check Collection<ResponseObject> size is equal for RequestObject duration,
                 * for example when user want to pre-calculate for 24 month , Collection<ResponseObject> should have 24 ResponseObject
                 */
                .andExpect(jsonPath("$",hasSize(requestObject.getDuration())))
                /**
                 * check all Collection members has value
                 */
                .andExpect(jsonPath("$[*].borrowerPaymentAmount").isNotEmpty())
                .andExpect(jsonPath("$[*].date").isNotEmpty())
                .andExpect(jsonPath("$[*].interest").isNotEmpty())
                .andExpect(jsonPath("$[*].principal").isNotEmpty())
                .andExpect(jsonPath("$[*].remainingOutstandingPrincipals").isNotEmpty());

    }
}
