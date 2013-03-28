package com.maestrodev.maestro.plugins.gitblit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.gitblit.client.GitblitClient;
import com.gitblit.models.RepositoryModel;

public class GitblitWorkerTest {

    JSONObject workitem;
    JSONObject fields;
    GitblitWorker worker;
    
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
	
	fields = new JSONObject();
	fields.put("server_name", "localhost");
	fields.put("url", "https://localhost:8443");
	fields.put("account", "admin");
	fields.put("password", "admin");
	fields.put("repository_name", "junit.git");
	fields.put("owner", "admin");
	fields.put("description", "junit");
	
	workitem = new JSONObject();
        workitem.put("fields", fields);
        
        worker = new GitblitWorker();	
        worker.setWorkitem(workitem);        
    }
    
    
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCreateRepository() throws IOException {
	GitblitWorker spy = spy(worker);
	GitblitClient clientMock = mock(GitblitClient.class);
	
	when(clientMock.createRepository(Mockito.any(RepositoryModel.class), Mockito.anyList())).thenReturn(true);
	when(spy.getGitblitClient()).thenReturn(clientMock);

        spy.createRepository();        
        assertThat(spy.getError(), nullValue());
        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCreateExistingRepository() throws IOException {
	GitblitClient clientMock = mock(GitblitClient.class);	
	when(clientMock.createRepository(Mockito.any(RepositoryModel.class), Mockito.anyList())).thenReturn(false);

	GitblitWorker workerSpy = spy(worker);
	when(workerSpy.getGitblitClient()).thenReturn(clientMock);

        workerSpy.createRepository();        
        assertThat(workerSpy.getError(), is("Unable to create repository"));
        
    }
    
    
    @Test
    public void testGetGitblitClient() {
	GitblitClient client = worker.getGitblitClient();	
	assertThat(client.account, is("admin"));
	assertThat(client.url, is("https://localhost:8443"));
    }
    

}
