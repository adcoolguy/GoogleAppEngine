package app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Movie", description="Movie for User")
@Searchable(alias = "2sharemov")
@Entity
public class Movie implements Cloneable, Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@SearchableId(name = "id")
    private Key key;
	@ManyToOne
	private User parent;
	//just for cloudendpoint
	private Long id;
	private boolean shuffled;
	//TODO this should be moved into Channel object
	private String channelPattern = "0";	//for channel display, automatic carry forward etc (advanced settings)

	private Long calendarId;
	//TODO this should be populated from Calendar object and to be renamed to calendarEventPattern
	private String eventPattern = "0";	//for sorting
	private boolean calendarAllDay;
	
	@SearchableProperty
	private String Title;
	@SearchableProperty
	private Text description;
	@SearchableProperty
	private Text searchResults;
	
	/** created just to facilitate the deletion of a legacy entity (serialized as String and passed to front end UI) */
    private String keyString;
/*	
	@SearchableProperty
	private String OriginalTitle;
	@SearchableProperty
	private String Media;
	@SearchableProperty
	private String Genre;
	@SearchableProperty
	private String Subgenre;
	@SearchableProperty
	private String Category;
	@SearchableProperty
	private String Edition;
	@SearchableProperty
	private String CatalogNo;
	@SearchableProperty
	private Integer Year;
	@SearchableProperty
	private String UPC;
	@SearchableProperty
	private String Country;
	@SearchableProperty
	private String OriginalLanguage;
	@SearchableProperty
	private String Language;
	@SearchableProperty
	private String Studio;
	@SearchableProperty
	private String Distributor;
	@SearchableProperty
	private String MPAA;
*/	
	@SearchableProperty
	private String URL;
	@SearchableProperty
	private Text subtitles;
/*	
	@SearchableProperty
	private String InternetID;
	@SearchableProperty
	private String ScriptUsed;
	@SearchableProperty
	private String WebLinkScript;
	@SearchableProperty
	private Text DirectLinkScript;
	@SearchableProperty
	private Long WebImported;
	@SearchableProperty
	private String Region;
	@SearchableProperty
	private String Length;
	@SearchableProperty
	private String VideoDVD;
	@SearchableProperty
	private String AudioDVD;
	@SearchableProperty
	private Integer MediaYear;
	@SearchableProperty
	private String Position;
	@SearchableProperty
	private String FreeTime;
	@SearchableProperty
	private String Mode;
	@SearchableProperty
	private String Packaging;
	@SearchableProperty
	private String VideoStandard;
	@SearchableProperty
	private String Chapter;
	@SearchableProperty
	private String Director;
	@SearchableProperty
	private String Producer;
	@SearchableProperty
	private String Musician;
	@SearchableProperty
	private String Writer;
	@SearchableProperty
	private String Photographer;
	@SearchableProperty
	private Text Comments;
	@SearchableProperty
	private Text Plot;
	@SearchableProperty
	private Text TagLine;
	@SearchableProperty
	private Text Notes;
	@SearchableProperty
	private String Cover;
	@SearchableProperty
	private Text Review;
	@SearchableProperty
	private String Location;
	@SearchableProperty
	private String Owner;
	@SearchableProperty
	private String Status;
	@SearchableProperty
	private Date DatePurchased;
	@SearchableProperty
	private String AquiredFrom;
	@SearchableProperty
	private BigDecimal Price;
	@SearchableProperty
	private String Value;
	@SearchableProperty
	private float Rating;
	@SearchableProperty
	private BigDecimal PersonalRating;
	@SearchableProperty
	private BigDecimal FilmQuality;
	@SearchableProperty
	private BigDecimal VideoQuality;
	@SearchableProperty
	private BigDecimal AudioQuality;
	@SearchableProperty
	private Date SeenWhen;
	@SearchableProperty
	private String SeenWhere;
	@SearchableProperty
	private Long Loan;
	@SearchableProperty
	private String Loaner;
	@SearchableProperty
	private Date DateLoan;
	@SearchableProperty
	private Date DateDue;
	@SearchableProperty
	private String MediaLabel;
	@SearchableProperty
	private Text Awards;
	@SearchableProperty
	private Text Actors;
	@SearchableProperty
	private Text Features;
	@SearchableProperty
	private Integer Color;
	@SearchableProperty
	private Integer DualSide;
	@SearchableProperty
	private Integer DualLayer;
	@SearchableProperty
	private Integer Seen;
	@SearchableProperty
	private Integer Marked;
*/	
	@SearchableProperty
	private Date Modified;
/*	
	@SearchableProperty
	private String Codec;
	@SearchableProperty
	private String Resolution;
	@SearchableProperty
	private String Bitrate;
	@SearchableProperty
	private String AudioCodec;
	@SearchableProperty
	private Long DolbyCodec;
	@SearchableProperty
	private float Filesize;
	@SearchableProperty
	private String SampleRate;
	@SearchableProperty
	private String AudioBitRate;
	@SearchableProperty
	private String Ripped;
	@SearchableProperty
	private Long Channels;
	@SearchableProperty
	private String MovieFile1;
	@SearchableProperty
	private String MovieFile2;
	@SearchableProperty
	private String MovieFile3;
	@SearchableProperty
	private String MovieFile4;
	@SearchableProperty
	private String MovieFile5;
	@SearchableProperty
	private String MovieFile6;
	@SearchableProperty
	private String TrailerFile1;
	@SearchableProperty
	private String TrailerFile2;
	@SearchableProperty
	private String Player;
	@SearchableProperty
	private Long VHSAdv;
	@SearchableProperty
	private Date DateInsert;
	@SearchableProperty
	private String FPS;
	@SearchableProperty
	private String Custom1;
	@SearchableProperty
	private String Custom2;
	@SearchableProperty
	private String Custom3;
	@SearchableProperty
	private String Custom4;
	@SearchableProperty
	private String Custom5;
	@SearchableProperty
	private String Custom6;
	@SearchableProperty
	private String Custom7;
	@SearchableProperty
	private String Custom8;
	@SearchableProperty
	private Text Custom9;
	@SearchableProperty
	private Text Custom10;
	@SearchableProperty
	private Text Custom11;
	@SearchableProperty
	private String ChainUsed;
	@SearchableProperty
	private String MovieMD51;
	@SearchableProperty
	private String MovieMD52;
	@SearchableProperty
	private String MovieMD53;
	@SearchableProperty
	private String MovieMD54;
	@SearchableProperty
	private String MovieMD55;
	@SearchableProperty
	private String MovieMD56;
	@SearchableProperty
	private String TrailerMD51;
	@SearchableProperty
	private String TrailerMD52;
	@SearchableProperty
	private Long Disks;
	@SearchableProperty
	private String ScreenRatio;
	@SearchableProperty
	private Text ScriptFields;
	@SearchableProperty
	private Double XHtml;
	@SearchableProperty
	private Long Wanted;
	@SearchableProperty
	private Long BoxSetID;
	@SearchableProperty
	private Text KeyWords;
	@SearchableProperty
	private String MCETitle;
	@SearchableProperty
	private String MovieFileLength1;
	@SearchableProperty
	private String MovieFileLength2;
	@SearchableProperty
	private String MovieFileLength3;
	@SearchableProperty
	private String MovieFileLength4;
	@SearchableProperty
	private String MovieFileLength5;
	@SearchableProperty
	private String MovieFileLength6;
	@SearchableProperty
	private String SimplyEmpty;
	@SearchableProperty
	private String BackCover;
	@SearchableProperty
	private String FanArt;
	@SearchableProperty
	private Long Movie3d;
	@SearchableProperty
	private String Premiered;
	@SearchableProperty
	private Long Tvshow;
	@SearchableProperty
	private Long Movie_id;
*/
	public User getParent() {
		return parent;
	}

	public void setParent(User parent) {
		this.parent = parent;
	}

	/** begin of resource sharing properties */
	private boolean shared;
	private String sharedPattern;
	private String owner;	//Parse user's id
	private String oid;	//Parse user's object id
	private String hash;
	private Date hashModified;
	/** end of resource sharing properties */
	
	@ApiModelProperty(value = "movie's id", required = true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
	@ApiModelProperty(value = "movie's subtitle", required = false)
	public Text getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(Text subtitles) {
		this.subtitles = subtitles;
	}

	public boolean getCalendarAllDay() {
		return calendarAllDay;
	}

	public void setCalendarAllDay(boolean allDay) {
		this.calendarAllDay = allDay;
	}

	public boolean isShuffled() {
		return shuffled;
	}

	public void setShuffled(boolean shuffled) {
		this.shuffled = shuffled;
	}

	public String getChannelPattern() {
		return channelPattern;
	}

	public void setChannelPattern(String channelPattern) {
		this.channelPattern = channelPattern;
	}

	public Long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}

	public String getEventPattern() {
		return eventPattern;
	}

	public void setEventPattern(String eventPattern) {
		this.eventPattern = eventPattern;
	}

	@ApiModelProperty(value = "movie's title", required = false)
	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	@ApiModelProperty(value = "movie's description", required = false)
	public Text getDescription() {
		return description;
	}

	public void setDescription(Text description) {
		this.description = description;
	}

	public Text getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(Text searchResults) {
		this.searchResults = searchResults;
	}
/*
	public String getOriginalTitle() {
		return OriginalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		OriginalTitle = originalTitle;
	}

	public String getMedia() {
		return Media;
	}

	public void setMedia(String media) {
		Media = media;
	}

	public String getGenre() {
		return Genre;
	}

	public void setGenre(String genre) {
		Genre = genre;
	}

	public String getSubgenre() {
		return Subgenre;
	}

	public void setSubgenre(String subgenre) {
		Subgenre = subgenre;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getEdition() {
		return Edition;
	}

	public void setEdition(String edition) {
		Edition = edition;
	}

	public String getCatalogNo() {
		return CatalogNo;
	}

	public void setCatalogNo(String catalogNo) {
		CatalogNo = catalogNo;
	}

	public Integer getYear() {
		return Year;
	}

	public void setYear(Integer year) {
		Year = year;
	}

	public String getUPC() {
		return UPC;
	}

	public void setUPC(String uPC) {
		UPC = uPC;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getOriginalLanguage() {
		return OriginalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) {
		OriginalLanguage = originalLanguage;
	}

	public String getLanguage() {
		return Language;
	}

	public void setLanguage(String language) {
		Language = language;
	}

	public String getStudio() {
		return Studio;
	}

	public void setStudio(String studio) {
		Studio = studio;
	}

	public String getDistributor() {
		return Distributor;
	}

	public void setDistributor(String distributor) {
		Distributor = distributor;
	}

	public String getMPAA() {
		return MPAA;
	}

	public void setMPAA(String mPAA) {
		MPAA = mPAA;
	}
*/

	@ApiModelProperty(value = "movie's URL", required = false)
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

/*	
	public String getInternetID() {
		return InternetID;
	}

	public void setInternetID(String IntegerernetID) {
		InternetID = IntegerernetID;
	}

	public String getScriptUsed() {
		return ScriptUsed;
	}

	public void setScriptUsed(String scriptUsed) {
		ScriptUsed = scriptUsed;
	}

	public String getWebLinkScript() {
		return WebLinkScript;
	}

	public void setWebLinkScript(String webLinkScript) {
		WebLinkScript = webLinkScript;
	}

	public Text getDirectLinkScript() {
		return DirectLinkScript;
	}

	public void setDirectLinkScript(Text directLinkScript) {
		DirectLinkScript = directLinkScript;
	}

	public Long getWebImported() {
		return WebImported;
	}

	public void setWebImported(Long webImported) {
		WebImported = webImported;
	}

	public String getRegion() {
		return Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public String getLength() {
		return Length;
	}

	public void setLength(String length) {
		Length = length;
	}

	public String getVideoDVD() {
		return VideoDVD;
	}

	public void setVideoDVD(String videoDVD) {
		VideoDVD = videoDVD;
	}

	public String getAudioDVD() {
		return AudioDVD;
	}

	public void setAudioDVD(String audioDVD) {
		AudioDVD = audioDVD;
	}

	public Integer getMediaYear() {
		return MediaYear;
	}

	public void setMediaYear(Integer mediaYear) {
		MediaYear = mediaYear;
	}

	public String getPosition() {
		return Position;
	}

	public void setPosition(String position) {
		Position = position;
	}

	public String getFreeTime() {
		return FreeTime;
	}

	public void setFreeTime(String freeTime) {
		FreeTime = freeTime;
	}

	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
	}

	public String getPackaging() {
		return Packaging;
	}

	public void setPackaging(String packaging) {
		Packaging = packaging;
	}

	public String getVideoStandard() {
		return VideoStandard;
	}

	public void setVideoStandard(String videoStandard) {
		VideoStandard = videoStandard;
	}

	public String getChapter() {
		return Chapter;
	}

	public void setChapter(String chapter) {
		Chapter = chapter;
	}

	public String getDirector() {
		return Director;
	}

	public void setDirector(String director) {
		Director = director;
	}

	public String getProducer() {
		return Producer;
	}

	public void setProducer(String producer) {
		Producer = producer;
	}

	public String getMusician() {
		return Musician;
	}

	public void setMusician(String musician) {
		Musician = musician;
	}

	public String getWriter() {
		return Writer;
	}

	public void setWriter(String writer) {
		Writer = writer;
	}

	public String getPhotographer() {
		return Photographer;
	}

	public void setPhotographer(String photographer) {
		Photographer = photographer;
	}

	public Text getComments() {
		return Comments;
	}

	public void setComments(Text comments) {
		Comments = comments;
	}

	public Text getPlot() {
		return Plot;
	}

	public void setPlot(Text plot) {
		Plot = plot;
	}

	public Text getTagLine() {
		return TagLine;
	}

	public void setTagLine(Text tagLine) {
		TagLine = tagLine;
	}

	public Text getNotes() {
		return Notes;
	}

	public void setNotes(Text notes) {
		Notes = notes;
	}

	public String getCover() {
		return Cover;
	}

	public void setCover(String cover) {
		Cover = cover;
	}

	public Text getReview() {
		return Review;
	}

	public void setReview(Text review) {
		Review = review;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		Owner = owner;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public Date getDatePurchased() {
		return DatePurchased;
	}

	public void setDatePurchased(Date datePurchased) {
		DatePurchased = datePurchased;
	}

	public String getAquiredFrom() {
		return AquiredFrom;
	}

	public void setAquiredFrom(String aquiredFrom) {
		AquiredFrom = aquiredFrom;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

	public float getRating() {
		return Rating;
	}

	public void setRating(float rating) {
		Rating = rating;
	}

	public BigDecimal getPersonalRating() {
		return PersonalRating;
	}

	public void setPersonalRating(BigDecimal personalRating) {
		PersonalRating = personalRating;
	}

	public BigDecimal getFilmQuality() {
		return FilmQuality;
	}

	public void setFilmQuality(BigDecimal filmQuality) {
		FilmQuality = filmQuality;
	}

	public BigDecimal getVideoQuality() {
		return VideoQuality;
	}

	public void setVideoQuality(BigDecimal videoQuality) {
		VideoQuality = videoQuality;
	}

	public BigDecimal getAudioQuality() {
		return AudioQuality;
	}

	public void setAudioQuality(BigDecimal audioQuality) {
		AudioQuality = audioQuality;
	}

	public Date getSeenWhen() {
		return SeenWhen;
	}

	public void setSeenWhen(Date seenWhen) {
		SeenWhen = seenWhen;
	}

	public String getSeenWhere() {
		return SeenWhere;
	}

	public void setSeenWhere(String seenWhere) {
		SeenWhere = seenWhere;
	}

	public Long getLoan() {
		return Loan;
	}

	public void setLoan(Long loan) {
		Loan = loan;
	}

	public String getLoaner() {
		return Loaner;
	}

	public void setLoaner(String loaner) {
		Loaner = loaner;
	}

	public Date getDateLoan() {
		return DateLoan;
	}

	public void setDateLoan(Date dateLoan) {
		DateLoan = dateLoan;
	}

	public Date getDateDue() {
		return DateDue;
	}

	public void setDateDue(Date dateDue) {
		DateDue = dateDue;
	}

	public String getMediaLabel() {
		return MediaLabel;
	}

	public void setMediaLabel(String mediaLabel) {
		MediaLabel = mediaLabel;
	}

	public Text getSubtitles() {
		return Subtitles;
	}

	public void setSubtitles(Text subtitles) {
		Subtitles = subtitles;
	}

	public Text getAwards() {
		return Awards;
	}

	public void setAwards(Text awards) {
		Awards = awards;
	}

	public Text getActors() {
		return Actors;
	}

	public void setActors(Text actors) {
		Actors = actors;
	}

	public Text getFeatures() {
		return Features;
	}

	public void setFeatures(Text features) {
		Features = features;
	}

	public Integer getColor() {
		return Color;
	}

	public void setColor(Integer color) {
		Color = color;
	}

	public Integer getDualSide() {
		return DualSide;
	}

	public void setDualSide(Integer dualSide) {
		DualSide = dualSide;
	}

	public Integer getDualLayer() {
		return DualLayer;
	}

	public void setDualLayer(Integer dualLayer) {
		DualLayer = dualLayer;
	}

	public Integer getSeen() {
		return Seen;
	}

	public void setSeen(Integer seen) {
		Seen = seen;
	}

	public Integer getMarked() {
		return Marked;
	}

	public void setMarked(Integer marked) {
		Marked = marked;
	}
*/

	public Date getModified() {
		return Modified;
	}

	public void setModified(Date modified) {
		Modified = modified;
	}

	
/*
	public String getCodec() {
		return Codec;
	}

	public void setCodec(String codec) {
		Codec = codec;
	}

	public String getResolution() {
		return Resolution;
	}

	public void setResolution(String resolution) {
		Resolution = resolution;
	}

	public String getBitrate() {
		return Bitrate;
	}

	public void setBitrate(String bitrate) {
		Bitrate = bitrate;
	}

	public String getAudioCodec() {
		return AudioCodec;
	}

	public void setAudioCodec(String audioCodec) {
		AudioCodec = audioCodec;
	}

	public Long getDolbyCodec() {
		return DolbyCodec;
	}

	public void setDolbyCodec(Long dolbyCodec) {
		DolbyCodec = dolbyCodec;
	}

	public float getFilesize() {
		return Filesize;
	}

	public void setFilesize(float filesize) {
		Filesize = filesize;
	}

	public String getSampleRate() {
		return SampleRate;
	}

	public void setSampleRate(String sampleRate) {
		SampleRate = sampleRate;
	}

	public String getAudioBitRate() {
		return AudioBitRate;
	}

	public void setAudioBitRate(String audioBitRate) {
		AudioBitRate = audioBitRate;
	}

	public String getRipped() {
		return Ripped;
	}

	public void setRipped(String ripped) {
		Ripped = ripped;
	}

	public Long getChannels() {
		return Channels;
	}

	public void setChannels(Long channels) {
		Channels = channels;
	}

	public String getMovieFile1() {
		return MovieFile1;
	}

	public void setMovieFile1(String movieFile1) {
		MovieFile1 = movieFile1;
	}

	public String getMovieFile2() {
		return MovieFile2;
	}

	public void setMovieFile2(String movieFile2) {
		MovieFile2 = movieFile2;
	}

	public String getMovieFile3() {
		return MovieFile3;
	}

	public void setMovieFile3(String movieFile3) {
		MovieFile3 = movieFile3;
	}

	public String getMovieFile4() {
		return MovieFile4;
	}

	public void setMovieFile4(String movieFile4) {
		MovieFile4 = movieFile4;
	}

	public String getMovieFile5() {
		return MovieFile5;
	}

	public void setMovieFile5(String movieFile5) {
		MovieFile5 = movieFile5;
	}

	public String getMovieFile6() {
		return MovieFile6;
	}

	public void setMovieFile6(String movieFile6) {
		MovieFile6 = movieFile6;
	}

	public String getTrailerFile1() {
		return TrailerFile1;
	}

	public void setTrailerFile1(String trailerFile1) {
		TrailerFile1 = trailerFile1;
	}

	public String getTrailerFile2() {
		return TrailerFile2;
	}

	public void setTrailerFile2(String trailerFile2) {
		TrailerFile2 = trailerFile2;
	}

	public String getPlayer() {
		return Player;
	}

	public void setPlayer(String player) {
		Player = player;
	}

	public Long getVHSAdv() {
		return VHSAdv;
	}

	public void setVHSAdv(Long vHSAdv) {
		VHSAdv = vHSAdv;
	}

	public Date getDateInsert() {
		return DateInsert;
	}

	public void setDateInsert(Date dateInsert) {
		DateInsert = dateInsert;
	}

	public String getFPS() {
		return FPS;
	}

	public void setFPS(String fPS) {
		FPS = fPS;
	}

	public String getCustom1() {
		return Custom1;
	}

	public void setCustom1(String custom1) {
		Custom1 = custom1;
	}

	public String getCustom2() {
		return Custom2;
	}

	public void setCustom2(String custom2) {
		Custom2 = custom2;
	}

	public String getCustom3() {
		return Custom3;
	}

	public void setCustom3(String custom3) {
		Custom3 = custom3;
	}

	public String getCustom4() {
		return Custom4;
	}

	public void setCustom4(String custom4) {
		Custom4 = custom4;
	}

	public String getCustom5() {
		return Custom5;
	}

	public void setCustom5(String custom5) {
		Custom5 = custom5;
	}

	public String getCustom6() {
		return Custom6;
	}

	public void setCustom6(String custom6) {
		Custom6 = custom6;
	}

	public String getCustom7() {
		return Custom7;
	}

	public void setCustom7(String custom7) {
		Custom7 = custom7;
	}

	public String getCustom8() {
		return Custom8;
	}

	public void setCustom8(String custom8) {
		Custom8 = custom8;
	}

	public Text getCustom9() {
		return Custom9;
	}

	public void setCustom9(Text custom9) {
		Custom9 = custom9;
	}

	public Text getCustom10() {
		return Custom10;
	}

	public void setCustom10(Text custom10) {
		Custom10 = custom10;
	}

	public Text getCustom11() {
		return Custom11;
	}

	public void setCustom11(Text custom11) {
		Custom11 = custom11;
	}

	public String getChainUsed() {
		return ChainUsed;
	}

	public void setChainUsed(String chainUsed) {
		ChainUsed = chainUsed;
	}

	public String getMovieMD51() {
		return MovieMD51;
	}

	public void setMovieMD51(String movieMD51) {
		MovieMD51 = movieMD51;
	}

	public String getMovieMD52() {
		return MovieMD52;
	}

	public void setMovieMD52(String movieMD52) {
		MovieMD52 = movieMD52;
	}

	public String getMovieMD53() {
		return MovieMD53;
	}

	public void setMovieMD53(String movieMD53) {
		MovieMD53 = movieMD53;
	}

	public String getMovieMD54() {
		return MovieMD54;
	}

	public void setMovieMD54(String movieMD54) {
		MovieMD54 = movieMD54;
	}

	public String getMovieMD55() {
		return MovieMD55;
	}

	public void setMovieMD55(String movieMD55) {
		MovieMD55 = movieMD55;
	}

	public String getMovieMD56() {
		return MovieMD56;
	}

	public void setMovieMD56(String movieMD56) {
		MovieMD56 = movieMD56;
	}

	public String getTrailerMD51() {
		return TrailerMD51;
	}

	public void setTrailerMD51(String trailerMD51) {
		TrailerMD51 = trailerMD51;
	}

	public String getTrailerMD52() {
		return TrailerMD52;
	}

	public void setTrailerMD52(String trailerMD52) {
		TrailerMD52 = trailerMD52;
	}

	public Long getDisks() {
		return Disks;
	}

	public void setDisks(Long disks) {
		Disks = disks;
	}

	public String getScreenRatio() {
		return ScreenRatio;
	}

	public void setScreenRatio(String screenRatio) {
		ScreenRatio = screenRatio;
	}

	public Text getScriptFields() {
		return ScriptFields;
	}

	public void setScriptFields(Text scriptFields) {
		ScriptFields = scriptFields;
	}

	public Double getXHtml() {
		return XHtml;
	}

	public void setXHtml(Double xHtml) {
		XHtml = xHtml;
	}

	public Long getWanted() {
		return Wanted;
	}

	public void setWanted(Long wanted) {
		Wanted = wanted;
	}

	public Long getBoxSetID() {
		return BoxSetID;
	}

	public void setBoxSetID(Long boxSetID) {
		BoxSetID = boxSetID;
	}

	public Text getKeyWords() {
		return KeyWords;
	}

	public void setKeyWords(Text keyWords) {
		KeyWords = keyWords;
	}

	public String getMCETitle() {
		return MCETitle;
	}

	public void setMCETitle(String mCETitle) {
		MCETitle = mCETitle;
	}

	public String getMovieFileLength1() {
		return MovieFileLength1;
	}

	public void setMovieFileLength1(String movieFileLength1) {
		MovieFileLength1 = movieFileLength1;
	}

	public String getMovieFileLength2() {
		return MovieFileLength2;
	}

	public void setMovieFileLength2(String movieFileLength2) {
		MovieFileLength2 = movieFileLength2;
	}

	public String getMovieFileLength3() {
		return MovieFileLength3;
	}

	public void setMovieFileLength3(String movieFileLength3) {
		MovieFileLength3 = movieFileLength3;
	}

	public String getMovieFileLength4() {
		return MovieFileLength4;
	}

	public void setMovieFileLength4(String movieFileLength4) {
		MovieFileLength4 = movieFileLength4;
	}

	public String getMovieFileLength5() {
		return MovieFileLength5;
	}

	public void setMovieFileLength5(String movieFileLength5) {
		MovieFileLength5 = movieFileLength5;
	}

	public String getMovieFileLength6() {
		return MovieFileLength6;
	}

	public void setMovieFileLength6(String movieFileLength6) {
		MovieFileLength6 = movieFileLength6;
	}

	public String getSimplyEmpty() {
		return SimplyEmpty;
	}

	public void setSimplyEmpty(String simplyEmpty) {
		SimplyEmpty = simplyEmpty;
	}

	public String getBackCover() {
		return BackCover;
	}

	public void setBackCover(String backCover) {
		BackCover = backCover;
	}

	public String getFanArt() {
		return FanArt;
	}

	public void setFanArt(String fanArt) {
		FanArt = fanArt;
	}

	public Long getMovie3d() {
		return Movie3d;
	}

	public void setMovie3d(Long movie3d) {
		Movie3d = movie3d;
	}

	public String getPremiered() {
		return Premiered;
	}

	public void setPremiered(String premiered) {
		Premiered = premiered;
	}

	public Long getTvshow() {
		return Tvshow;
	}

	public void setTvshow(Long tvshow) {
		Tvshow = tvshow;
	}

	public Long getMovie_id() {
		return Movie_id;
	}

	public void setMovie_id(Long movie_id) {
		Movie_id = movie_id;
	}
*/

	public boolean getShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public String getSharedPattern() {
		return sharedPattern;
	}

	public void setSharedPattern(String sharedPattern) {
		this.sharedPattern = sharedPattern;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Date getHashModified() {
		return hashModified;
	}

	public void setHashModified(Date hashModified) {
		this.hashModified = hashModified;
	}

	@Override
	public String toString() {
		return "Movie [key=" + key + ", parent=" + parent + ", id=" + id
				+ ", channelPattern=" + channelPattern + ", calendarId="
				+ calendarId 
				+ ", eventPattern=" + eventPattern 
				+ ", Title="
				+ Title + ", description=" + description + ", searchResults="
				+ searchResults + ", keyString=" + keyString + ", URL=" + URL
				+ ", Modified=" + Modified + ", shared=" + shared
				+ ", sharedPattern=" + sharedPattern + ", owner=" + owner
				+ ", oid=" + oid + ", hash=" + hash + ", hashModified="
				+ hashModified + "]";
	}

	public String getKeyString() {
		return keyString;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Movie other = (Movie) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
