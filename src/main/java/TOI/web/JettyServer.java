package TOI.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created with IntelliJ IDEA. User: W.k Date: 13-5-12 Time: 12:14 To change
 * this template use File | Settings | File Templates.
 */
public class JettyServer {

	public static void main(String[] args) {
		Server server = new Server();
		server.setThreadPool(createThreadPool());
		server.addConnector(createConnector());
		server.setHandler(createHandlers());
		server.setStopAtShutdown(true);
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

	}

	private static ThreadPool createThreadPool() {
		// TODO: You should configure these appropriately
		// for your environment - this is an example only
		QueuedThreadPool _threadPool = new QueuedThreadPool();
		_threadPool.setMinThreads(5);
		_threadPool.setMaxThreads(9);
		return _threadPool;
	}

	private static SelectChannelConnector createConnector() {
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(8014);
		connector.setHost("localhost");
		connector.setForwarded(true);
		connector.setReuseAddress(true);
		connector.setAcceptors(4);
		connector.setMaxIdleTime(60);
		// connector.setLowResourcesMaxIdleTime(10);
		// connector.setAcceptorPriorityOffset(10);
		// connector.setAcceptQueueSize(10000);
		// connector.setMaxBuffers(1024);
		// connector.setRequestBufferSize(1000);
		// connector.setRequestHeaderSize(1000);
		// connector.setResponseBufferSize(1000);
		// connector.setResponseHeaderSize(1000);
		connector.setName("main-jetty");

		return connector;
	}

	private static HandlerCollection createHandlers() {

		WebAppContext _ctx = new WebAppContext();
		_ctx.setContextPath("/ikea");
		_ctx.setWar("src/main/webapp");

		List<Handler> _handlers = new ArrayList<Handler>();
		_handlers.add(_ctx);

		HandlerList _contexts = new HandlerList();
		_contexts.setHandlers(_handlers.toArray(new Handler[1]));

		HandlerCollection _result = new HandlerCollection();
		_result.setHandlers(new Handler[] { _contexts });

		return _result;
	}

	private static RequestLog createRequestLog() {
		NCSARequestLog _log = new NCSARequestLog();

		File _logPath = new File(".");
		_logPath.getParentFile().mkdirs();

		_log.setFilename(_logPath.getPath());
		_log.setRetainDays(7);
		_log.setExtended(false);
		_log.setAppend(true);
		_log.setLogTimeZone("GMT");
		_log.setLogLatency(true);
		return _log;
	}

}
