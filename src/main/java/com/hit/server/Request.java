package com.hit.server;
import java.io.Serializable;

public class Request<T> extends java.lang.Object implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 8553301636784820828L;
	private java.util.Map<String,String> headers;
    private T body;

    public Request(java.util.Map<String,String> headers, T body)
    {
        this.headers = headers;
        this.body = body;
    }

    public T getBody()
    {
        return this.body;
    }

    public java.util.Map<String,String> getHeaders()
    {
        return this.headers;
    }

    public void	setBody(T body)
    {
        this.body = body;
    }

    public void	setHeaders(java.util.Map<String,String> headers)
    {
        this.headers = headers;
    }

    @Override
    public java.lang.String toString()
    {
        return "Request{" +
                "headers=" + headers.toString() +
                ", body=" + body.toString() +
                '}';
    }
}