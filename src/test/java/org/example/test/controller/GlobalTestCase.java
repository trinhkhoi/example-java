package org.example.test.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.example.test.ExampleDataSetLoader;
import org.example.test.TruncateDatabaseService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@DbUnitConfiguration(dataSetLoader = ExampleDataSetLoader.class)
public class GlobalTestCase {

	/** {@link WebApplicationContext} wac. */
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private TruncateDatabaseService truncateDatabaseService;

	/** {@link MockMvc} mockMvc. */
	private MockMvc mockMvc;

	/**
	 * setup.
	 *
	 * @throws Exception
	 *
	 */
	@Before
	public void setup(){
		setMockMvc(MockMvcBuilders.webAppContextSetup(wac).build());
	}

	/**
	 * Memory dump.
	 */
	@After
	public void afterTest() throws Exception {
		truncateDatabaseService.truncate();
	}

	/**
	 * @return the mockMvc
	 */
	public MockMvc getMockMvc() {
		return mockMvc;
	}

	/**
	 * @param mockMvc
	 *            the mockMvc to set
	 */
	public void setMockMvc(final MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

}
