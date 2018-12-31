package com.hit.dm;

@SuppressWarnings("serial")
public class DataModel <T> implements java.io.Serializable
{
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

	public void setDataModelId(java.lang.Long id)
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
		DataModel<T> otherModel = (DataModel<T>) obj;
		if (this.dataModelId != otherModel.dataModelId) {
			return false;
		}
		return true;

	}
}
