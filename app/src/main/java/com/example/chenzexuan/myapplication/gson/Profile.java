package com.example.chenzexuan.myapplication.gson;

import com.google.gson.JsonElement;

import java.util.ArrayList;

class Profile {
  String receivedLikes;
  String receivedLikesRank;
  String occupation;
  String job;
  String zodiac;
  Work work;
  Studies studies;
  String hometown;
  String hangouts;
  ArrayList<Tags> tags;
  ArrayList<JsonElement> social;
  ArrayList<Answer> answers;
  String scenarios;
  MutualContacts mutualContacts;
  MutualContacts publicMoments;
}
