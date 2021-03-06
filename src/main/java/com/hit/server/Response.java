package com.hit.server;
import java.io.Serializable;

public class Response<T> implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2630554794510354418L;
	private java.util.Map<java.lang.String,java.lang.String> headers;
    private T body;

    public Response(java.util.Map<java.lang.String,java.lang.String> headers, T body)
    {
        this.headers = headers;
        this.body = body;
    }

    public T getBody()
    {
        return this.body;
    }

    public java.util.Map<java.lang.String,java.lang.String> getHeaders()
    {
        return this.headers;
    }

    public void	setBody(T body)
    {
        this.body = body;
    }

    public void	setHeaders(java.util.Map<java.lang.String,java.lang.String> headers)
    {
        this.headers = headers;
    }

    @Override
    public java.lang.String toString()
    {
        return "Response{" +
                "headers=" + headers.toString() +
                ", body=" + body.toString() +
                '}';
    }
}