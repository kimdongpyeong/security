var Test4Page;
Test4Page = Vue.component("test4-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test4/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "youtubeLink": "",
                    "gauth": null
                },

            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            "authenticate": function() {
                return gapi.auth2.getAuthInstance()
                    .signIn({scope: "https://www.googleapis.com/auth/youtube.readonly"})
                    .then(function() { console.log("Sign-in successful"); },
                          function(err) { console.error("Error signing in", err); });
              },
              "loadClient": function() {
                  console.log(gapi.client)
                gapi.client.setApiKey("AIzaSyDKcavx0GqX40U-QGfEVltzdK_yL2aB2Eo");
                return gapi.client.load("https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest")
                    .then(function() { console.log("GAPI client loaded for API"); },
                          function(err) { console.error("Error loading GAPI client for API", err); });
              },
              // Make sure the client is loaded and sign-in is complete before calling this method.
              "execute": function() {
                  return gapi.client.youtube.videos.list({
                    "part": [
                      "liveStreamingDetails,id"
                    ],
                    "id": "ZKrq7dMKR0g",
                  })
                    .then(function(response) {
                            // Handle the results here (response.result has the parsed body).
                            console.log("Response", response);
                          },
                          function(err) { console.error("Execute error", err);});

//                  return gapi.client.youtube.liveChatMessages.list({
//                    "part": [
//                      "snippet,id"
//                    ],
//                    "liveChatId": "Cg0KC1pLcnE3ZE1LUjBnKicKGFVDYkhOMXNUdVB5U0twbnFLU3AwSkhSURILWktycTdkTUtSMGc",
//                  })
//                    .then(function(response) {
//                            // Handle the results here (response.result has the parsed body).
//                            console.log("Response", response);
//                          },
//                          function(err) { console.error("Execute error", err);});
              },
              "setting": function() {
                  gapi.load('client:auth2', function() {
                      var gauth = gapi.auth2.init({
                          client_id: '369059048668-ni1eugv2c4bhf717au7dnhsbmp24keoc.apps.googleusercontent.com'
                      });
                  });
              },
        },
        "mounted": async function () {
            this.setting();
        },
        "created": async function () {
        },
    });
});