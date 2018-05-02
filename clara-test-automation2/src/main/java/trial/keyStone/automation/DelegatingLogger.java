package trial.keyStone.automation;

import org.slf4j.Logger;
import org.slf4j.Marker;

public abstract class DelegatingLogger implements Logger
{
	private Logger	logger;

	public DelegatingLogger(Logger logger)
	{
		this.logger = logger;
	}

	protected abstract void beforeLogging();

	public synchronized String getName()
	{
		return logger.getName();
	}

	public synchronized boolean isTraceEnabled()
	{
		return logger.isTraceEnabled();
	}

	public synchronized void trace(String msg)
	{
		beforeLogging();
		logger.trace(msg);
	}

	public synchronized void trace(String format, Object arg)
	{
		beforeLogging();
		logger.trace(format, arg);
	}

	public synchronized void trace(String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.trace(format, arg1, arg2);
	}

	public synchronized void trace(String format, Object[] arguments)
	{
		beforeLogging();
		logger.trace(format, arguments);
	}

	public synchronized void trace(String msg, Throwable t)
	{
		beforeLogging();
		logger.trace(msg, t);
	}

	public synchronized boolean isTraceEnabled(Marker marker)
	{
		beforeLogging();
		return logger.isTraceEnabled(marker);
	}

	public synchronized void trace(Marker marker, String msg)
	{
		beforeLogging();
		logger.trace(marker, msg);
	}

	public synchronized void trace(Marker marker, String format, Object arg)
	{
		beforeLogging();
		logger.trace(marker, format, arg);
	}

	public synchronized void trace(Marker marker, String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.trace(marker, format, arg1, arg2);
	}

	public synchronized void trace(Marker marker, String format, Object... argArray)
	{
		beforeLogging();
		logger.trace(marker, format, argArray);
	}

	public synchronized void trace(Marker marker, String msg, Throwable t)
	{
		beforeLogging();
		logger.trace(marker, msg, t);
	}

	public synchronized boolean isDebugEnabled()
	{
		return logger.isDebugEnabled();
	}

	public synchronized void debug(String msg)
	{
		beforeLogging();
		logger.debug(msg);
	}

	public synchronized void debug(String format, Object arg)
	{
		beforeLogging();
		logger.debug(format, arg);
	}

	public synchronized void debug(String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.debug(format, arg1, arg2);
	}

	public synchronized void debug(String format, Object[] arguments)
	{
		beforeLogging();
		logger.debug(format, arguments);
	}

	public synchronized void debug(String msg, Throwable t)
	{
		beforeLogging();
		logger.debug(msg, t);
	}

	public synchronized boolean isDebugEnabled(Marker marker)
	{
		beforeLogging();
		return logger.isDebugEnabled(marker);
	}

	public synchronized void debug(Marker marker, String msg)
	{
		beforeLogging();
		logger.debug(marker, msg);
	}

	public synchronized void debug(Marker marker, String format, Object arg)
	{
		beforeLogging();
		logger.debug(marker, format, arg);
	}

	public synchronized void debug(Marker marker, String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.debug(marker, format, arg1, arg2);
	}

	public synchronized void debug(Marker marker, String format, Object[] arguments)
	{
		beforeLogging();
		logger.debug(marker, format, arguments);
	}

	public synchronized void debug(Marker marker, String msg, Throwable t)
	{
		beforeLogging();
		logger.debug(marker, msg, t);
	}

	public synchronized boolean isInfoEnabled()
	{
		return logger.isInfoEnabled();
	}

	public synchronized void info(String msg)
	{
		beforeLogging();
		logger.info(msg);
	}

	public synchronized void info(String format, Object arg)
	{
		beforeLogging();
		logger.info(format, arg);
	}

	public synchronized void info(String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.info(format, arg1, arg2);
	}

	public synchronized void info(String format, Object[] arguments)
	{
		beforeLogging();
		logger.info(format, arguments);
	}

	public synchronized void info(String msg, Throwable t)
	{
		beforeLogging();
		logger.info(msg, t);
	}

	public synchronized boolean isInfoEnabled(Marker marker)
	{
		beforeLogging();
		return logger.isInfoEnabled(marker);
	}

	public synchronized void info(Marker marker, String msg)
	{
		beforeLogging();
		logger.info(marker, msg);
	}

	public synchronized void info(Marker marker, String format, Object arg)
	{
		beforeLogging();
		logger.info(marker, format, arg);
	}

	public synchronized void info(Marker marker, String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.info(marker, format, arg1, arg2);
	}

	public synchronized void info(Marker marker, String format, Object[] arguments)
	{
		beforeLogging();
		logger.info(marker, format, arguments);
	}

	public synchronized void info(Marker marker, String msg, Throwable t)
	{
		beforeLogging();
		logger.info(marker, msg, t);
	}

	public synchronized boolean isWarnEnabled()
	{
		return logger.isWarnEnabled();
	}

	public synchronized void warn(String msg)
	{
		logger.warn(msg);
	}

	public synchronized void warn(String format, Object arg)
	{
		beforeLogging();
		logger.warn(format, arg);
	}

	public synchronized void warn(String format, Object[] arguments)
	{
		beforeLogging();
		logger.warn(format, arguments);
	}

	public synchronized void warn(String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.warn(format, arg1, arg2);
	}

	public synchronized void warn(String msg, Throwable t)
	{
		beforeLogging();
		logger.warn(msg, t);
	}

	public synchronized boolean isWarnEnabled(Marker marker)
	{
		beforeLogging();
		return logger.isWarnEnabled(marker);
	}

	public synchronized void warn(Marker marker, String msg)
	{
		beforeLogging();
		logger.warn(marker, msg);
	}

	public synchronized void warn(Marker marker, String format, Object arg)
	{
		beforeLogging();
		logger.warn(marker, format, arg);
	}

	public synchronized void warn(Marker marker, String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.warn(marker, format, arg1, arg2);
	}

	public synchronized void warn(Marker marker, String format, Object[] arguments)
	{
		beforeLogging();
		logger.warn(marker, format, arguments);
	}

	public synchronized void warn(Marker marker, String msg, Throwable t)
	{
		beforeLogging();
		logger.warn(marker, msg, t);
	}

	public synchronized boolean isErrorEnabled()
	{
		return logger.isErrorEnabled();
	}

	public synchronized void error(String msg)
	{
		beforeLogging();
		logger.error(msg);
	}

	public synchronized void error(String format, Object arg)
	{
		beforeLogging();
		logger.error(format, arg);
	}

	public synchronized void error(String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.error(format, arg1, arg2);
	}

	public synchronized void error(String format, Object[] arguments)
	{
		beforeLogging();
		logger.error(format, arguments);
	}

	public synchronized void error(String msg, Throwable t)
	{
		beforeLogging();
		logger.error(msg, t);
	}

	public synchronized boolean isErrorEnabled(Marker marker)
	{
		return logger.isErrorEnabled(marker);
	}

	public synchronized void error(Marker marker, String msg)
	{
		beforeLogging();
		logger.error(marker, msg);
	}

	public synchronized void error(Marker marker, String format, Object arg)
	{
		beforeLogging();
		logger.error(marker, format, arg);
	}

	public synchronized void error(Marker marker, String format, Object arg1, Object arg2)
	{
		beforeLogging();
		logger.error(marker, format, arg1, arg2);
	}

	public synchronized void error(Marker marker, String format, Object[] arguments)
	{
		beforeLogging();
		logger.error(marker, format, arguments);
	}

	public synchronized void error(Marker marker, String msg, Throwable t)
	{
		beforeLogging();
		logger.error(marker, msg, t);
	}

}
