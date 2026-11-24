package com.jobtracker.util;

import java.util.List;
import java.util.Map;

public class Constants {

  public static final List<String> STATUSES = List.of(
      "Applied", "Assessment", "Interview", "Selected", "Rejected", "Offer Received");

  public static final List<String> JOB_TYPES = List.of(
      "Full-time", "Part-time", "Internship", "Contract", "Remote");

  public static final Map<String, String> BADGES = Map.of(
      "Applied", "text-bg-primary",
      "Assessment", "text-bg-info",
      "Interview", "text-bg-warning",
      "Selected", "text-bg-success",
      "Rejected", "text-bg-danger",
      "Offer Received", "text-bg-dark");
}