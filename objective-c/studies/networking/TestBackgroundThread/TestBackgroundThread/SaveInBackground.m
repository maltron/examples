//
//  SaveInBackground.m
//  TestBackgroundThread
//
//  Created by Mauricio Leal on 2/24/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "SaveInBackground.h"
#import "Person.h"

@interface SaveInBackground()
@property (nonatomic, assign, getter=isRunning) BOOL running;
@property (nonatomic, assign, getter=isCancelled) BOOL cancelled;
@property (readonly, nonatomic, strong) NSManagedObjectContext *parentContext, *backgroundContext;

@end

@implementation SaveInBackground
@synthesize backgroundContext = _backgroundContext;
@synthesize parentContext = _parentContext;

-(id)init:(NSManagedObjectContext *)parent {
    self = [super init];
    if(self) {
        _parentContext = parent;
        _backgroundContext = [[NSManagedObjectContext alloc] initWithConcurrencyType:NSPrivateQueueConcurrencyType];
        [_backgroundContext setParentContext:parent];
        
        // Add an Observer in case of 
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(mergeAll:) name:NSManagedObjectContextDidSaveNotification object:_backgroundContext];
        
    }
    
    return self;
}

-(void)start {
    if(_running) return;
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSLog(@">>> [SaveInBackground run] START ++++");
        _running = YES; _cancelled = NO;

        NSFetchRequest *fetchRequest = [[NSFetchRequest alloc] init];
        [fetchRequest setEntity:[Person entity:self.backgroundContext]];
        
        NSPredicate *predicate = [NSPredicate predicateWithFormat:@"]
        
        
        _running = NO;
        NSLog(@">>> [SaveInBackground run] STOP  ----");
    });
}


-(void)stop {
    _cancelled = YES;
}

-(void)mergeAll:(NSNotification *)notification {
    NSLog(@">>> [SaveInBackground mergeAll]");
    
    if([NSThread isMainThread]) {
        NSLog(@">>> [SaveInBackground mergeAll] NSThread isMainThread]");
        [self.parentContext mergeChangesFromContextDidSaveNotification:notification];
        
    } else {
        NSLog(@">>> [SaveInBackground mergeAll] Dispatch Async");
        dispatch_async(dispatch_get_main_queue(), ^{
           [self.parentContext mergeChangesFromContextDidSaveNotification:notification];
        });
    }
}

@end
