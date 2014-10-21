var express = require('express')
var app = express()
var bignum = require('bignum');
var moment = require('moment');
var pub_key = '6876766832351765396496377534476050002970857483815262918450355869850085167053394672634315391224052153';

app.enable('trust proxy');

app.get('/q1', function (req, res) {
  var key = req.query.key;
  var team_info = 'VivaLavida,';
  var team_aws = '1360-3153-2241,6890-3858-3604,3861-9435-4536\n';
  var date = moment().format('YYYY-MM-DD HH:mm:ss')
  if (key == undefined) {key = '';}
  var ans = bignum(key).div(pub_key);
  res.send(ans.toString() + "\n" + team_info + team_aws + date + "\n");
})

app.get('/index.html', function(req, res) {
  res.send('');
})

app.get('/q2', function (req, res) {
  var userid = req.query.userid;
  var tweet_time = req.query.tweet_time;
  var date = tweet_time.split("+");
  var time = date[0];
  res.send(userid + "\n" + time);
})
var server = app.listen(8080, function () {

  var host = server.address().address
  var port = server.address().port

})
