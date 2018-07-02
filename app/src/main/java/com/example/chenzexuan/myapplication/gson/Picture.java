package com.example.chenzexuan.myapplication.gson;

import com.google.gson.JsonElement;

import java.util.ArrayList;

class Picture {
  String url;
  String mediaType;
  ArrayList<String> size;
  String duration;
  ArrayList<Parameter> parameters;
  JsonElement attachments;
}
