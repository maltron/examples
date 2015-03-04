//
//  PersonService.m
//  TestBackgroundThread
//
//  Created by Mauricio Leal on 2/24/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "PersonService.h"
#import "Person.h"
#import "AppDelegate.h"

@implementation PersonService
@synthesize personContext = _personContext;

-(id)init {
    self = [super init];
    if(self) {
        _personContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
        [_personContext setParentContext:[[AppDelegate instance] mainContext]];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(mergeAll:) name:NSManagedObjectContextDidSaveNotification object:_personContext];
    }
    
    return self;
}

-(void)fetchAll:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSURL *url = [delegate url];
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
    
    [self logRequest:@"[PersonService fetchAll]" httpMethod:@"GET" url:url data:nil];
    NSURLSessionTask *task = [[delegate session] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        [self logResponse:@"[PersonService fetchAll]" response:response data:data];
        
        if([self isSuccessfull:response]) {
            // Step #1: Erase all current data
            NSLog(@">>> [PersonService fetchAll] Step #1: Deleting All Local");
            [self deleteAllLocal];
            
            // Step #2: Parse all the content
            NSLog(@">>> [PersonService fetchAll] Step #2: Parsing all Remotes");
            error = nil;
            id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
            if(json && [json isKindOfClass:[NSArray class]]) {
                NSArray *results = (NSArray *)json;
                NSLog(@">>> [PersonService fetchAll] Parsing %li components", (long)[results count]);
                for(NSDictionary *dict in results) {
                    Person *newPerson = [Person newPerson:[self personContext]];
                    [newPerson parseJSONfromDictionary:dict];
                }
                
            }
            
            // Step #3: Save it all
            NSLog(@">>> [PersonService fetchAll] Step #3: Save it all");
            error = nil; // Recycling Error instance ??????
            // This operation should trigger mergeAll: method call
            if([self.personContext hasChanges] && ![self.personContext save:&error]) {
                NSLog(@"### [PersonService save] ERROR:%@, %@", error, [error userInfo]);
                abort();
            }
        }
        
        if(completionHandler) completionHandler(data, response, error);
        
    }];
    [task resume];
}

-(void)deleteAllLocal {
    NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
    [fetchRequest setEntity:[Person entity:[self personContext]]];
    
    NSArray *result = [self.personContext executeFetchRequest:fetchRequest error:nil];
    for(Person *person in result) [self.personContext deleteObject:person];
    
    //    NSError *error = nil;
    //    if([self.personContext hasChanges] && ![self.personContext save:&error]) {
    //        NSLog(@"### [PersonService deleteAllLocal] ERROR:%@, %@", error, [error userInfo]);
    //        abort();
    //    }
}

-(void)findFirstName:(NSString *)firstName andLastName:(NSString *)lastName completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSURL *url = [delegate urlFindFirst:firstName andLastName:lastName];
    NSURLRequest *request = [[NSURLRequest alloc] initWithURL:url];
    
    [self logRequest:@"[PersonService findFirstName]" httpMethod:@"GET" url:url data:nil];
    NSURLSessionTask *task = [[delegate session] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        [self logResponse:@"[PersonService findFirstName]" response:response data:data];
        
        // SUCCES
        if([self isSuccessfull:response]) {
            NSLog(@">>> [PersonService findFirstName] FOUND. Not a good thing");
            
        } else if([self isError:response]) {
            NSLog(@">>> [PersonService findFirstName] NOT FOUND. It's a good thing");
            
        }
        
        // Call completionHandler, somewhere else
        if(completionHandler) completionHandler(data, response, error);
        
    }];
    [task resume];
}

-(void)save:(Person *)person isInsert:(BOOL)isInsert completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *respose, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSURL *url = [delegate url];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:isInsert ? @"POST" : @"PUT"];
    NSData *data = [person toJSON];
    
    [self logRequest:@"[PersonService save]" httpMethod:isInsert ? @"POST" : @"PUT" url:url data:data];
    NSURLSessionUploadTask *task = [[delegate session] uploadTaskWithRequest:request fromData:data completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        [self logResponse:@"[PersonService save]" response:response data:data];
        
        // SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS
        if([self isSuccessfull:response]) {
            NSLog(@">>> [PersonService save] SUCCESS");
            [person parseJSON:data]; // It will update the data
            
            error = nil; // Recycling Error instance ??????
            // This operation should trigger mergeAll: method call
            if([self.personContext hasChanges] && ![self.personContext save:&error]) {
                NSLog(@"### [PersonService save] ERROR:%@, %@", error, [error userInfo]);
                abort();
            }
        }
        
        // Call completionHandler somewhere else
        if(completionHandler) completionHandler(data, response, error);
        
    }];
    [task resume];
}

-(void)delete:(NSManagedObjectID *)ID completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *respose, NSError *error))completionHandler {
    AppDelegate *delegate = [AppDelegate instance];
    
    NSError *error = nil;
    Person *person = (Person *)[self.personContext existingObjectWithID:ID error:&error];
    if(error) {
        NSLog(@"### [PeronService delete] ERROR:%@, %@", error, [error userInfo]);
        abort();
    }
    
    NSURL *url = [delegate urlDelete:[person iD]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:url];
    [request setHTTPMethod:@"DELETE"];
    
    [self logRequest:@"[PersonService delete]" httpMethod:@"DELETE" url:url data:nil];
    NSURLSessionTask *task = [[delegate session] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *r, NSError *error) {
        NSHTTPURLResponse *response = (NSHTTPURLResponse *)r;
        [self logResponse:@"[PersonService delete]" response:response data:data];
        
        // SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS
        if([self isSuccessfull:response]) {
            NSLog(@">>> [PersonService delete] SUCCESS");
            [self.personContext deleteObject:person];
            
            error = nil; // Recycling Error instance ?????
            // This operation should trigger mergeAll: method call
            if([self.personContext hasChanges] && ![self.personContext save:&error]) {
                NSLog(@"### [PersonService save] ERROR:%@, %@", error, [error userInfo]);
                abort();
            }
        }
        
        // Call completionHandler somewhere else
        if(completionHandler) completionHandler(data, response, error);
        
    }];
    [task resume];
}

-(void)mergeAll:(NSNotification *)notification {
    NSLog(@">>> [PersonService mergeAll]");
    NSManagedObjectContext *context = [[AppDelegate instance] mainContext];
    
    if([NSThread isMainThread]) {
        NSLog(@">>> [PersonService mergeAll] NSThread isMainThread");
        [context mergeChangesFromContextDidSaveNotification:notification];
        
    } else {
        NSLog(@">>> [PersonService mergeAll] Dispatch Async");
        dispatch_async(dispatch_get_main_queue(), ^{
            [context mergeChangesFromContextDidSaveNotification:notification];
        });
    }
}


@end
