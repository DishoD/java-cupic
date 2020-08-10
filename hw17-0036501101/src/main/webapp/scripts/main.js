$(document).ready(function(){
    let $tagSelector = $("#tagSelector");
    let $photoSelector = $("#photoSelector");
    let $photo = $("#photo");
    let tagCounter = 0;
    
    function loadPhoto(id) {
    	$photo.empty();
    	$($photo).attr("class", "");
    	
    	$.ajax({
        	type: 'GET',
        	url: 'rest/photo/' + id,
        	success: function(photo) {
        		$photo.append('<img src="photo?id='+id+'" class="photoBig">');
        		$photo.append('<h3>'+photo.description+'</h3>');
        		$photo.append('<p>'+photo.tags+'<p>');
        		
        		$('html, body').animate({
        	        scrollTop: $("#photo").offset().top
        	    }, 500);
        	},
        	error: function() {
        		alert("Error loading photo from the server!");
        	}
        });
    }
    
    function addThumbnail(id) {
    	let img = new Image();
    	img.src = "loading.gif";
    	$(img).attr("class", "thumbnail");
		img.onclick = function(){
    		loadPhoto(id);
    	};
    	$photoSelector.append(img);
    	
    	let imgDownload = new Image();
    	imgDownload.onload = function() {
    		img.src = this.src;
    	};
    	imgDownload.onerror = function() {
    		setTimeout(function() {imgDownload.src = "thumbnail?id="+id;}, 1500);
    	};
    	imgDownload.src = "thumbnail?id="+id;
    }
    
    function loadThumbnails(tag) {
    	$photoSelector.empty();
    	$($photoSelector).attr("class", "");
    	$photo.empty();
    	$($photo).attr("class", "empty");
    	
    	$.ajax({
        	type: 'GET',
        	url: 'rest/tags/' + tag,
        	success: function(photos) {
        		$.each(photos, function(i, photo) {
        			addThumbnail(photo);
        		});
        	},
        	error: function() {
        		alert("Error loading thumbnails from the server!");
        	}
        });
    }
    
    function addTagBtn(name) {
    	$tagSelector.append('<input type="button" class="tagBtns" id="'+tagCounter+'" value="'+name+'">');
    	$("#"+tagCounter).click(function(){
    		loadThumbnails(name);
    	});
    	tagCounter++;
    }
    
    $.ajax({
    	type: 'GET',
    	url: 'rest/tags',
    	success: function(tags) {
    		$.each(tags, function(i, tag) {
    			addTagBtn(tag);
    		});
    	},
    	error: function() {
    		alert("Error loading tags from the server!");
    	}
    });
    
});