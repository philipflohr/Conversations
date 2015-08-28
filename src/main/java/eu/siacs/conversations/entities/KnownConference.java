package eu.siacs.conversations.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.siacs.conversations.utils.UIHelper;
import eu.siacs.conversations.xml.Element;
import eu.siacs.conversations.xmpp.jid.Jid;

public class KnownConference extends Element implements ListItem {

	private Conversation mJoinedConversation;

	public KnownConference(final Jid jid) {
		super("conference");
		this.setAttribute("jid", jid.toString());
	}

	@Override
	public int compareTo(final ListItem another) {
		return this.getDisplayName().compareToIgnoreCase(
				another.getDisplayName());
	}

	@Override
	public String getDisplayName() {
		if (this.mJoinedConversation != null
				&& (this.mJoinedConversation.getMucOptions().getSubject() != null)) {
			return this.mJoinedConversation.getMucOptions().getSubject();
		} else if (getName() != null) {
			return getName();
		} else {
			return this.getJid().getLocalpart();
		}
	}

	@Override
	public Jid getJid() {
		return this.getAttributeAsJid("jid");
	}

	@Override
	public List<Tag> getTags() {
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (Element element : getChildren()) {
			if (element.getName().equals("group") && element.getContent() != null) {
				String group = element.getContent();
				tags.add(new Tag(group, UIHelper.getColorForName(group)));
			}
		}
		return tags;
	}

	public String getNick() {
		return this.findChildContent("nick");
	}

	public void setNick(String nick) {
		Element element = this.findChild("nick");
		if (element == null) {
			element = this.addChild("nick");
		}
		element.setContent(nick);
	}

	public boolean autojoin() {
		return this.getAttributeAsBoolean("autojoin");
	}

	public String getPassword() {
		return this.findChildContent("password");
	}

	public void setPassword(String password) {
		Element element = this.findChild("password");
		if (element != null) {
			element.setContent(password);
		}
	}

	public boolean match(String needle) {
		if (needle == null) {
			return true;
		}
		needle = needle.toLowerCase(Locale.US);
		final Jid jid = getJid();
		return (jid != null && jid.toString().contains(needle)) ||
			getDisplayName().toLowerCase(Locale.US).contains(needle) ||
			matchInTag(needle);
	}

	private boolean matchInTag(String needle) {
		needle = needle.toLowerCase(Locale.US);
		for (Tag tag : getTags()) {
			if (tag.getName().toLowerCase(Locale.US).contains(needle)) {
				return true;
			}
		}
		return false;
	}

	public Conversation getConversation() {
		return this.mJoinedConversation;
	}

	public void setConversation(Conversation conversation) {
		this.mJoinedConversation = conversation;
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public void setName(String name) {
		this.name = name;
	}

	public void unregisterConversation() {
		if (this.mJoinedConversation != null) {
			this.mJoinedConversation.deregisterWithBookmark();
		}
	}
}
