//
//  MyViewController.m
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "MyViewController.h"
#import "AutoLayout.h"

#define ADDRESS @"http://localhost:8080/samplejson/rest/resource"

@interface MyViewController()
@property (nonatomic, strong) UIBarButtonItem *buttonRequest, *buttonRight;

@end

@implementation MyViewController

-(void)viewDidLoad {
    [super viewDidLoad];
    
    _buttonRequest = [[UIBarButtonItem alloc] initWithTitle:@"Request" style:UIBarButtonItemStylePlain target:self action:@selector(actionLeft:)];
    _buttonRight = [[UIBarButtonItem alloc] initWithTitle:@"Right" style:UIBarButtonItemStylePlain target:self action:@selector(actionRight:)];
    [self.navigationItem setLeftBarButtonItem:_buttonRequest];
    [self.navigationItem setRightBarButtonItem:_buttonRight];
    [self.navigationItem setTitle:@"Title"];
}

-(void)actionLeft:(id)sender {
    NSLog(@"[MyViewController actionLeft] START");
    
    // Step #1
    NSURL *url = [NSURL URLWithString:ADDRESS];
//    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
    
    NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
    NSURLSession *session = [NSURLSession sessionWithConfiguration:configuration delegate:self delegateQueue:nil];
    NSURLSessionDataTask *task = [session dataTaskWithURL:url completionHandler:^(NSData *data, NSURLResponse *res, NSError *error) {
        if(!error) {
            // SUCCESS
            NSHTTPURLResponse *response = (NSHTTPURLResponse *)res;
            NSLog(@"HTTP Response:%@", response);
            if([response statusCode] == 200) {
                NSError *errorjson = nil;
                id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&errorjson];
                if([json isKindOfClass:[NSArray class]]) {
                    NSLog(@"JSON is NSArray");
                    NSArray *array = (NSArray *)json;
                    for(int i=0; i < [array count]; i++)
                        NSLog(@"Array %d:%@", i, array[i]);
                    
                } else if([json isKindOfClass:[NSDictionary class]]) {
                    NSLog(@"JSON is NSDictionary");
                    NSDictionary *dictionary = (NSDictionary *)json;
                    for(NSString *key in dictionary)
                        NSLog(@"Dictionary %@:%@", key, [dictionary objectForKey:key]);
                    
                }
            }
            
        } else {
            // FAILURE
            NSLog(@"ERROR: %@", error);
        }
    }];
    [task resume];
    NSLog(@"[MyViewController actionLeft] END");
}

-(void)actionRight:(id)sender {
    NSLog(@"[MyViewController actionRight]");
    
}

// URL SESSION DELEGATE URL SESSION DELEGATE URL SESSION DELEGATE URL SESSION DELEGATE
//  URL SESSION DELEGATE URL SESSION DELEGATE URL SESSION DELEGATE URL SESSION DELEGATE

/* The last message a session receives.  A session will only become
 * invalid because of a systemic error or when it has been
 * explicitly invalidated, in which case the error parameter will be nil.
 */
- (void)URLSession:(NSURLSession *)session didBecomeInvalidWithError:(NSError *)error {
    NSLog(@"[NSURLSessionDelegate didBecomeInvalidWithError]");
}

/* If implemented, when a connection level authentication challenge
 * has occurred, this delegate will be given the opportunity to
 * provide authentication credentials to the underlying
 * connection. Some types of authentication will apply to more than
 * one request on a given connection to a server (SSL Server Trust
 * challenges).  If this delegate message is not implemented, the
 * behavior will be to use the default handling, which may involve user
 * interaction.
 */
- (void)URLSession:(NSURLSession *)session didReceiveChallenge:(NSURLAuthenticationChallenge *)challenge
 completionHandler:(void (^)(NSURLSessionAuthChallengeDisposition disposition, NSURLCredential *credential))completionHandler {
    NSLog(@"[NSURLSessionDelegate didReceiveChallenge]");
}

/* If an application has received an
 * -application:handleEventsForBackgroundURLSession:completionHandler:
 * message, the session delegate will receive this message to indicate
 * that all messages previously enqueued for this session have been
 * delivered.  At this time it is safe to invoke the previously stored
 * completion handler, or to begin any internal updates that will
 * result in invoking the completion handler.
 */
- (void)URLSessionDidFinishEventsForBackgroundURLSession:(NSURLSession *)session {
    NSLog(@"[URLSessionDidFinishEventsForBackgroundURLSession session]");
}

// URL SESSION DATA DELEGATE URL SESSION DATA DELEGATE URL SESSION DATA DELEGATE URL
//   URL SESSION DATA DELEGATE URL SESSION DATA DELEGATE URL SESSION DATA DELEGATE URL

/* The task has received a response and no further messages will be
 * received until the completion block is called. The disposition
 * allows you to cancel a request or to turn a data task into a
 * download task. This delegate message is optional - if you do not
 * implement it, you can get the response as a property of the task.
 *
 * This method will not be called for background upload tasks (which cannot be converted to download tasks).
 */
- (void)URLSession:(NSURLSession *)session dataTask:(NSURLSessionDataTask *)dataTask
didReceiveResponse:(NSURLResponse *)response
 completionHandler:(void (^)(NSURLSessionResponseDisposition disposition))completionHandler {
    NSLog(@"[URLSessionDataDelegate didReceiveResponse]");
    
}

/* Notification that a data task has become a download task.  No
 * future messages will be sent to the data task.
 */
- (void)URLSession:(NSURLSession *)session dataTask:(NSURLSessionDataTask *)dataTask
didBecomeDownloadTask:(NSURLSessionDownloadTask *)downloadTask {
    NSLog(@"[URLSessionDataDelegate didBecomeDownloadTask]");
}

/* Sent when data is available for the delegate to consume.  It is
 * assumed that the delegate will retain and not copy the data.  As
 * the data may be discontiguous, you should use
 * [NSData enumerateByteRangesUsingBlock:] to access it.
 */
- (void)URLSession:(NSURLSession *)session dataTask:(NSURLSessionDataTask *)dataTask
    didReceiveData:(NSData *)data {
    NSLog(@"[URLSessionDataDelegate didReceiveData]");
}

/* Invoke the completion routine with a valid NSCachedURLResponse to
 * allow the resulting data to be cached, or pass nil to prevent
 * caching. Note that there is no guarantee that caching will be
 * attempted for a given resource, and you should not rely on this
 * message to receive the resource data.
 */
- (void)URLSession:(NSURLSession *)session dataTask:(NSURLSessionDataTask *)dataTask
 willCacheResponse:(NSCachedURLResponse *)proposedResponse
 completionHandler:(void (^)(NSCachedURLResponse *cachedResponse))completionHandler {
    NSLog(@"[URLSessionDataDelegate willCacheResponse]");
}

@end
