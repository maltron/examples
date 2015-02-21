//
//  PersonCRUD.m
//  TestSendDataMOC
//
//  Created by Mauricio Leal on 2/16/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonCRUD.h"
#import "AppDelegate.h"

@implementation PersonCRUD

-(id)initWithContext:(NSManagedObjectContext *)context {
    self = [super init];
    if(self) {
        // Subscribe to change notifications
        [[NSNotificationCenter defaultCenter] addObserver:self
            selector:@selector(saveContext:)  name:NSManagedObjectContextDidSaveNotification
                                            object:context];
    }
    
    return self;
}

-(void)save:(NSNumber *)iD firstName:(NSString *)firstName andLastName:(NSString *)lastName completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    // The ID tells if the operation is an INSERT (POST) or UPDATE (PUT)
    BOOL isInsert = iD == nil;
    
    NSURL *url = [delegate url];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:isInsert ? @"POST" : @"PUT"];
    
    NSLog(@">>> [PersonCRUD save] REQUEST: <%@> %@", isInsert ? @"POST" : @"PUT", url != nil ? [url absoluteString] : @"NIL");
    
    NSData *data = [Person toJSON:iD firstName:firstName andLastName:lastName];
    NSURLSessionUploadTask *task = [[delegate session]
        uploadTaskWithRequest:request fromData:data
        completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSLog(@">>> [Person save] RESPONSE: %li %@", (long)[response statusCode], [NSHTTPURLResponse localizedStringForStatusCode:[response statusCode]]);
        
        if([response statusCode] >= 200 && [response statusCode] < 300) {
            NSLog(@">>> [Person save] SUCCESS. Saving locally");
            
            // Creating a new Person in a background context
            Person *newPerson = isInsert ? [Person newPerson:[delegate backgroundContext]] :
                                            [[Person alloc] init];
            [newPerson parseFromJSON:data];
        }
        
        // Despite whatever happen, call the completion handler
        if(completionHandler) completionHandler(data, response, error);
        
    }];
    [task resume];
}

-(void)remove:(Person *)person completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSURL *url = [delegate urlDelete:[person iD]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:@"DELETE"];
    
    NSLog(@">>> [PersonCRUD remove] REQUEST:%@", url != nil ? [url absoluteString] : @"NIL");
    NSURLSessionDataTask *task = [[delegate session] dataTaskWithRequest:request
        completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
            NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSLog(@">>> [Person remove] RESPONSE: %li %@", (long)[response statusCode], [NSHTTPURLResponse localizedStringForStatusCode:[response statusCode]]);

            
        if([response statusCode] >= 200 && [response statusCode] < 300) {
            NSLog(@">>> [PersonCRUD remove] SUCCESS. Deleting locally");
            [[delegate backgroundContext] deleteObject:person];
        }
        
        // Despite whatver happens, call the completion handler
        if(completionHandler) completionHandler(data, response, error);
            
    }];
    [task resume];
}

-(void)findByFirst:(NSString *)firstName andLastName:(NSString *)lastName completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *r, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSURL *url = [delegate urlFindFirst:firstName andLastName:lastName];
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
    
    NSLog(@">>> [PersonCRUD findByFirst:%@ andLastName:%@]",firstName, lastName);
    NSLog(@">>> [PersonCRUD REQUEST:%@", url != nil ? [url absoluteString] : @"NIL");
    NSURLSessionDataTask *task = [[delegate session] dataTaskWithRequest:request
        completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        NSLog(@">>> [PersonCRUD findByFirst:andLastName] RESPONSE: %li %@", (long)[response statusCode], [NSHTTPURLResponse localizedStringForStatusCode:[response statusCode]]);
        
        // It's up to the block what to do next
        if(completionHandler) completionHandler(data, response, error);
        
    }];
    [task resume];
}

-(void)saveContext:(NSNotification *)notification {
    NSLog(@">>> [PersonCRUD saveContext]");
    NSManagedObjectContext *context = [[AppDelegate instance] mainContext];
    if(!context) {
        NSLog(@"### [PersonCRUD saveContext] Managed Object Context is NIL");
        return;
    }
    
    if([NSThread isMainThread]) {
        NSLog(@">>> [PersonCRUD saveContext] Running on Main Thread");
        [context mergeChangesFromContextDidSaveNotification:notification];
        
    } else {
        NSLog(@">>> [PersonCRUD saveContext] Dispatching Async into Main Thread");
        dispatch_async(dispatch_get_main_queue(), ^{
            [context mergeChangesFromContextDidSaveNotification:notification];
        });
    }
}

@end
