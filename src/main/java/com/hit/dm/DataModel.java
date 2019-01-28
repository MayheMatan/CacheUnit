package com.hit.dm;

public class DataModel <T> extends java.lang.Object implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -129900713111766418L;
	private Long dataModelId;
	private T content;

	public DataModel(Long id, T content)
	{
		this.dataModelId = id;
		this.content = content;
	}

	public Long getDataModelId() {
		return dataModelId;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content)
	{
		this.content = content;
	}

	public void setDataModelId(Long id)
	{
		this.dataModelId = id;
	}
	@Override
	public String toString() {
		return "DataModel [id=" + dataModelId + ", content=" + content + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DataModel<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		DataModel<T> otherModel = (DataModel<T>) obj;
		if (this.dataModelId != otherModel.dataModelId) {
			return false;
		}
		return true;

	}
}
