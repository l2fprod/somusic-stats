function(doc) {
  emit([doc.createdAt, doc.artist, doc.album, doc.title], doc.count);
}
