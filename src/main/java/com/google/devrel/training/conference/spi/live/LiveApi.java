package com.google.devrel.training.conference.spi.live;

import static com.google.devrel.training.conference.service.OfyService.ofy;
import static com.google.devrel.training.conference.utils.Http.*;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import com.google.api.server.spi.auth.common.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.devrel.training.conference.Constants;
import com.google.devrel.training.conference.authenticator.MyAuthenticator;
import com.google.devrel.training.conference.domain.live.Live;
import com.google.devrel.training.conference.domain.security.Account;
import com.google.devrel.training.conference.domain.stream.Stream;
import com.googlecode.objectify.Key;

import org.json.*;

/**
 * Defines Live APIs.
 */
@Api(name = "live", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID,
		Constants.API_EXPLORER_CLIENT_ID }, authenticators = {
				MyAuthenticator.class }, description = "API for the lives")
public class LiveApi {

	@ApiMethod(name = "getLive")
	public Live getLive(final User user, @Named("websafeKey") String websafeKey) throws UnauthorizedException, BadRequestException, NotFoundException {

		// TODO update youtube key
		String youtubeKey = "AIzaSyDBvU-UnCmxvPTORWRhAoXbPw4T49IJGCI";

		
		
		// If not signed in, throw a 401 error.
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		Key<Stream> key = Key.create(websafeKey);
		Stream stream = (Stream) ofy().load().key(key).now();

		// Not found
		if (stream == null) {
			throw new NotFoundException("No stream found with key: " + websafeKey);
		}

		// Check if it's owner
		Key<Account> accountKey = Key.create(Account.class, Long.valueOf(user.getId()));
		if (!(stream.getAccountKey().equivalent(accountKey))) {
			throw new UnauthorizedException("Authorization required -> Not owner");
		}
		
		String plateform_stream = "";
		String urlAPI = "";
		String replyAPI = "";
		HashMap<String, String> headers = new HashMap<String, String>();

		try {
			plateform_stream = getDomainName(stream.getPlateform());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		String live;
		JSONObject json;
		Live is_live = new Live();

		switch (plateform_stream) {
		/*case "hitbox.tv":
			// TODO
			// decouper les URIs plus proprement

			urlAPI = "http://api.hitbox.tv/media/live/" + stream.getPlateform().substring(21);
			replyAPI = callApiGet(urlAPI, headers);
			json= new JSONObject(replyAPI);
			live = json.getJSONArray("livestream").get("media_is_live").textValue();
			if (Integer.parseInt(live) == 0) {
				is_live = "0";
			} else {
				is_live = "1";
			}
			break;*/
		case "twitch.tv":
			urlAPI = "https://api.twitch.tv/kraken/streams/" + stream.getPlateform().substring(21);
			replyAPI = callApiGet(urlAPI, headers);
			json= new JSONObject(replyAPI);
			live = json.get("stream").toString();
			if (live == "null") {
				is_live.setLive(false);
			} else {
				is_live.setLive(true);
			}
			break;
		case "dailymotion.com":
			urlAPI = "https://api.dailymotion.com/video/" + stream.getPlateform().substring(33)
					+ "?fields=id,onair,audience";
			replyAPI = callApiGet(urlAPI, headers);
			json= new JSONObject(replyAPI);
			live = json.get("onair").toString();
			if (Boolean.parseBoolean(live) == false) {
				is_live.setLive(false);
			} else {
				is_live.setLive(true);
			}
			break;
		case "games.dailymotion.com":
			urlAPI = "https://api.dailymotion.com/video/" + stream.getPlateform().substring(34)
					+ "?fields=id,onair,audience";
			replyAPI = callApiGet(urlAPI, headers);
			json= new JSONObject(replyAPI);
			live = json.get("onair").toString();
			if (Boolean.parseBoolean(live) == false) {
				is_live.setLive(false);
			} else {
				is_live.setLive(true);
			}
			break;
		case "youtube.com":
			urlAPI = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId="
					+ stream.getPlateform().substring(32) + "&eventType=live&type=video&key=" + youtubeKey;
			replyAPI = callApiGet(urlAPI, headers);
			System.out.println(replyAPI);
			json= new JSONObject(replyAPI);
			live = json.get("items").toString();
			if (live.contains("[]")) {
				is_live.setLive(false);
			} else {
				is_live.setLive(true);
			}
			break;
		default:
			throw new BadRequestException("Plateforme de stream non prise en charge");
		}
		
		return is_live;

	}

}
