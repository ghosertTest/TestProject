package assis;

public class Suggestion {

	private String sugId;

	private String sugName;

	private String sugEmail;

	private String sugTime;

	private String sugIp;

	private String suggestion;

	public Suggestion(String sugId, String sugName, String sugEmail,
			String sugTime, String sugIp, String suggestion) {

		this.sugId = sugId;
		this.sugName = sugName;
		this.sugEmail = sugEmail;
		this.sugTime = sugTime;
		this.sugIp = sugIp;
		this.suggestion = suggestion;
	}

	public String getSugId() {

		return sugId;
	}

	public String getSugName() {

		return sugName;
	}

	public String getSugEmail() {

		return sugEmail;
	}

	public String getSugTime() {

		return sugTime;
	}

	public String getSugIp() {

		return sugIp;
	}

	public String getSuggestion() {

		return suggestion;
	}
}