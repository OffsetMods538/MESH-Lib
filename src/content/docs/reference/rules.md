---
title: Rules
---

Rules tell the mod how to match requests.

They are json objects that consist of their identifier `type` and any fields specific to each rule.  
Below is a list of all supported rules:

|                     |                          |
|---------------------|--------------------------|
| [`domain`](#domain) | Matches the domain name  |
| [`path`](#path)     | Matches the request path |

### Domain
```json
{
  "domain": "example.com",
  "type": "domain"
}
```
This rule matches based on the domain name.

### Path
```json
{
  "path": "/hello",
  "type": "path"
}
```
This rule matches based on the request path.
