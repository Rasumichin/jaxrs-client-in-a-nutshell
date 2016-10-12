package de.jsmithy.rest.jaxrs.nutshell.client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This simple POJO class has nothing to do with the framework. It is still needed to test the automatic conversion from
 * JSON payload to a custom type (such as this class).
 * Visit "http://jsonplaceholder.typicode.com/posts" to find the REST service provider that delivers a JSON
 * representation of this class.
 * 
 * @author Erik Lotz
 * @since 2016-06-04
 * 
 */
@XmlRootElement(name = "post")
public class JsonPlaceholderPost {
	private int id;
	private int userId;
	private String title;
	private String body;
	
	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int anId) {
		id = anId;
	}
	
	@XmlElement
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int anUserId) {
		userId = anUserId;
	}
	
	@XmlElement
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String aTitle) {
		title = aTitle;
	}
	
	@XmlElement
	public String getBody() {
		return body;
	}
	
	public void setBody(String aBody) {
		body = aBody;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + id;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonPlaceholderPost other = (JsonPlaceholderPost) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + id + ", userId=" + userId + ", title=" + title + "]";
	}
}
